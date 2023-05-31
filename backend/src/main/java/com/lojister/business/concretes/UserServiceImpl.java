package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.CurrentUserDto;
import com.lojister.controller.account.RegistrationRequestDto;
import com.lojister.model.dto.client.ClientDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.enums.*;
import com.lojister.core.exception.AccountNotActiveException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.exception.UnApprovedDriverException;
import com.lojister.core.exception.UnauthorizedTransactionException;
import com.lojister.mapper.ClientMapper;
import com.lojister.mapper.DriverMapper;
import com.lojister.model.entity.User;
import com.lojister.model.entity.VerificationToken;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.driver.Driver;
import com.lojister.repository.account.UserRepository;
import com.lojister.business.abstracts.UserService;
import com.lojister.business.abstracts.VerificationTokenService;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.validator.formatter.PhoneFormatter;
import com.lojister.core.validator.DuplicateEmailValidator;
import com.lojister.core.validator.DuplicatePhoneValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final ClientMapper clientMapper;
    private final DriverMapper driverMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender mailSender;
    private final SecurityContextUtil securityContextUtil;
    private final PhoneFormatter phoneFormatter;
    private final DuplicatePhoneValidator duplicatePhoneValidator;
    private final DuplicateEmailValidator duplicateEmailValidator;




    @Override
    public DriverDto registerDriver(RegistrationRequestDto registrationRequestDto, String siteURL) throws UnsupportedEncodingException, MessagingException {

        String formattedNumber = phoneFormatter.format(registrationRequestDto.getPhone());
        duplicatePhoneValidator.validate(formattedNumber);
        duplicateEmailValidator.validate(registrationRequestDto.getEmail());

        Driver driver = new Driver();
        driver.setPhone(registrationRequestDto.getPhone());
        driver.setFirstName(registrationRequestDto.getFirstName());
        driver.setLastName(registrationRequestDto.getLastName());
        driver.setEmail(registrationRequestDto.getEmail());
        driver.setPhone(formattedNumber);
        driver.setUserRegionType(registrationRequestDto.getUserRegionType());
        driver.setStatus(DriverStatus.REGISTERED);
        driver.setRole(Role.ROLE_DRIVER);
        driver.setDriverTitle(DriverTitle.BOSS);
        driver.setEnabled(false);
        driver.setMailConfirmed(false);
        driver.setPhoneConfirmed(false);
        driver.setPassword(bCryptPasswordEncoder.encode(registrationRequestDto.getPassword()));
        driver = userRepository.save(driver);
        VerificationToken verificationToken = new VerificationToken(driver);
        verificationTokenService.saveRepo(verificationToken);

        sendVerificationEmail(verificationToken, siteURL);

        return driverMapper.entityToDto(driver);
    }

    @Override
    public ClientDto registerClient(RegistrationRequestDto registrationRequestDto, String siteURL) throws UnsupportedEncodingException, MessagingException {

        String formattedNumber = phoneFormatter.format(registrationRequestDto.getPhone());
        duplicatePhoneValidator.validate(formattedNumber);
        duplicateEmailValidator.validate(registrationRequestDto.getEmail());

        Client client = new Client();

        client.setFirstName(registrationRequestDto.getFirstName());
        client.setLastName(registrationRequestDto.getLastName());

        client.setPhone(formattedNumber);

        client.setRole(Role.ROLE_CLIENT);
        client.setEnabled(false);
        client.setMailConfirmed(false);
        client.setPhoneConfirmed(false);
        client.setUserRegionType(registrationRequestDto.getUserRegionType());
        client.setPassword(bCryptPasswordEncoder.encode(registrationRequestDto.getPassword()));
        client = userRepository.save(client);

        VerificationToken verificationToken = new VerificationToken(client);
        verificationTokenService.saveRepo(verificationToken);

        sendVerificationEmail(verificationToken, siteURL);

        return clientMapper.entityToDto(client);

    }

    @Override
    public User findDataById(Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.user.EntityNotFoundException"));
        }
    }

    @Override
    public User saveRepo(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailRepo(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean phoneVerification(String phone) {
        duplicatePhoneValidator.validate("+"+phone);
        return Boolean.TRUE;
    }

    @Override
    public Boolean emailVerification(String email) {
        duplicateEmailValidator.validate(email);
        return Boolean.TRUE;
    }

    @Override
    public void changeLanguage(Language language) {
        User user = securityContextUtil.getCurrentUser();
        user.setLanguage(language);
        userRepository.save(user);
    }

    @Override
    public void sendVerificationEmail(VerificationToken verificationToken, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = verificationToken.getUser().getEmail();
        String fromAddress = "noreply@lojister.com";
        String senderName = "Lojister";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", verificationToken.getUser().getFirstName() + " " + verificationToken.getUser().getLastName());
        String verifyURL = siteURL + "/api/V2/account/verify?code=" + verificationToken.getVerificationToken();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("Email has been sent");
    }

    @Override
    public Boolean verify(String verificationCode) {

        VerificationToken verificationTokenData = verificationTokenService.findDataByVerificationToken(verificationCode);


        User user = verificationTokenData.getUser();

        if (!(verificationTokenService.isExpiredToken(verificationTokenData.getId()))) {

            user.setEnabled(true);
            user.setMailConfirmed(true);
            userRepository.save(user);
            verificationTokenService.delete(verificationTokenData);
            return true;
        } else {

            return false;
        }


    }

    @Override
    public CurrentUserDto getMe() {

        Role role = securityContextUtil.getCurrentUserRole();

        CurrentUserDto currentUserDto = new CurrentUserDto();

        if (role == Role.ROLE_DRIVER || role == Role.ROLE_DRIVER_EMPLOYEE) {

            Driver driver = securityContextUtil.getCurrentDriver();
            currentUserDto.setId(driver.getId());
            currentUserDto.setFirstName(driver.getFirstName());
            currentUserDto.setLastName(driver.getLastName());
            currentUserDto.setPhone(driver.getPhone());
            currentUserDto.setEmail(driver.getEmail());
            currentUserDto.setPhoneConfirmed(driver.getPhoneConfirmed());
            currentUserDto.setMailConfirmed(driver.getMailConfirmed());
            currentUserDto.setRole(driver.getRole());
            currentUserDto.setDriverTitle(driver.getDriverTitle());
            currentUserDto.setDriverStatus(driver.getStatus());
            currentUserDto.setStatusDescription(driver.getStatusDescription());
            currentUserDto.setCompanyId(driver.getCompany().getId());
            currentUserDto.setCompanyCommercialTitle(driver.getCompany().getCommercialTitle());
            currentUserDto.setCommissionRate(driver.getCompany().getCommissionRate());
            return currentUserDto;

        } else if (role == Role.ROLE_CLIENT ||role == Role.ROLE_CLIENT_EMPLOYEE) {

            Client client = securityContextUtil.getCurrentClient();
            currentUserDto.setId(client.getId());
            currentUserDto.setFirstName(client.getFirstName());
            currentUserDto.setCurrent(client.getCurrent());
            currentUserDto.setTl(client.getTl());
            currentUserDto.setEuro(client.getEuro());
            currentUserDto.setUsd(client.getUsd());
            currentUserDto.setLastName(client.getLastName());
            currentUserDto.setPhone(client.getPhone());
            currentUserDto.setEmail(client.getEmail());
            currentUserDto.setPhoneConfirmed(client.getPhoneConfirmed());
            currentUserDto.setMailConfirmed(client.getMailConfirmed());
            currentUserDto.setRole(client.getRole());
            currentUserDto.setClientType(client.getClientType());

            if (client.getClientType() == ClientType.PERSON) {

                return currentUserDto;


            } else if (client.getClientType() == ClientType.CORPORATE) {

                currentUserDto.setCompanyId(client.getCompany().getId());
                currentUserDto.setCompanyCommercialTitle(client.getCompany().getCommercialTitle());

                return currentUserDto;

            } else {
                throw new UnauthorizedTransactionException(Translator.toLocale("lojister.user.UnauthorizedTransactionException"));
            }

        } else if (role == Role.ROLE_ADMIN) {

            User user = securityContextUtil.getCurrentUser();

            currentUserDto.setId(user.getId());
            currentUserDto.setFirstName(user.getFirstName());
            currentUserDto.setLastName(user.getLastName());
            currentUserDto.setRole(user.getRole());
            return currentUserDto;

        } else {

            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.user.UnauthorizedTransactionException"));
        }
    }


    @Override
    public Boolean updateFirebaseToken(String firebaseToken) {

        User currentUser = securityContextUtil.getCurrentUser();

        currentUser.setFirebaseToken(firebaseToken);
        userRepository.save(currentUser);

        return true;
    }


    @Override
    public void userEnabledCheck(User user) {

        if (!user.getEnabled()) {
            throw new AccountNotActiveException(Translator.toLocale("lojister.user.AccountNotActiveException"));
        }
    }


    @Override
    public void userDriverRoleStatusEnabledCheck(User user) {

        if (user.getRole() == Role.ROLE_DRIVER_EMPLOYEE) {

            Driver driver = (Driver) user;
            if (driver.getStatus() != DriverStatus.ACCEPTED) {
                throw new UnApprovedDriverException(Translator.toLocale("lojister.user.UnApprovedDriverException"));
            }
        }
    }
    @Override
    public List<User> userDrivers() {
        return userRepository.userRole(Role.ROLE_DRIVER);
    }


}
