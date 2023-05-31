package com.lojister.business.concretes;

import com.lojister.business.abstracts.*;
import com.lojister.controller.account.ClientRegisterRequest;
import com.lojister.core.i18n.Translator;
import com.lojister.core.validator.DuplicateCitizenIdValidator;
import com.lojister.model.dto.CompanyValidatorDto;
import com.lojister.model.dto.ForgotMyPasswordMailDto;
import com.lojister.controller.account.RegistrationRequestDto;
import com.lojister.model.dto.TokenResponseDto;
import com.lojister.model.dto.client.ClientDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.controller.account.LoginRequestDto;
import com.lojister.model.dto.request.ResetPasswordDto;
import com.lojister.model.entity.client.ClientAccountSetting;
import com.lojister.model.entity.client.ClientNotificationSetting;
import com.lojister.model.entity.driver.DriverAccountSetting;
import com.lojister.model.entity.driver.DriverNotificationSetting;
import com.lojister.model.enums.ClientType;
import com.lojister.model.enums.DriverStatus;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.enums.Role;
import com.lojister.core.exception.EmptyStringException;
import com.lojister.core.exception.ExpiredVerificationTokenException;
import com.lojister.core.exception.IncorrectEntryException;
import com.lojister.core.exception.WrongPersonTypeException;
import com.lojister.mapper.ClientMapper;
import com.lojister.mapper.DriverMapper;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.entity.*;
import com.lojister.core.security.JwtTokenUtil;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.validator.formatter.PhoneFormatter;
import com.lojister.core.util.sender.ForgotPasswordEmailSender;
import com.lojister.core.validator.DuplicateCompanyValidator;
import com.lojister.core.validator.DuplicateEmailValidator;
import com.lojister.core.validator.DuplicatePhoneValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PhoneFormatter phoneFormatter;
    private final DuplicatePhoneValidator duplicatePhoneValidator;
    private final DuplicateEmailValidator duplicateEmailValidator;
    private final ClientMapper clientMapper;
    private final DriverMapper driverMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CompanyService companyService;
    private final ClientService clientService;
    private final BankInformationService bankInformationService;
    private final DuplicateCompanyValidator duplicateCompanyValidator;
    private final ForgotPasswordEmailSender forgotPasswordEmailSender;
    private final VerificationTokenService verificationTokenService;
    private final SecurityContextUtil securityContextUtil;
    private final DriverService driverService;
    private final DuplicateCitizenIdValidator duplicateCitizenIdValidator;



    @Override
    public TokenResponseDto login(LoginRequestDto request) {

        try {
            String formattedNumber = phoneFormatter.format(request.getPhoneNumber());

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formattedNumber, request.getPassword()));
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = customUserDetails.getUser();

            userService.userEnabledCheck(currentUser);
            userService.userDriverRoleStatusEnabledCheck(currentUser);


            final String token = jwtTokenUtil.generateToken(currentUser, request.getRememberMe());

            return new TokenResponseDto(currentUser.getPhone(), token,currentUser.getRole());

        } catch (BadCredentialsException e) {

            throw new IncorrectEntryException(Translator.toLocale("lojister.account.IncorrectEntryException"));
        }
    }

    @Override
    public ClientDto registerClientWeb(ClientRegisterRequest clientRegisterRequest) {

        duplicatePhoneValidator.validate(clientRegisterRequest.getPhone());
        duplicateEmailValidator.validate(clientRegisterRequest.getEmail());
        duplicateCitizenIdValidator.validate(clientRegisterRequest.getCitizenId());
        Client client = new Client();



        client.setCurrent(false);
        client.setTl(0.0);
        client.setUsd(0.0);
        client.setEuro(0.0);

        client.setFirstName(clientRegisterRequest.getFirstName());
        client.setLastName(clientRegisterRequest.getLastName());
        client.setPhone(phoneFormatter.format(clientRegisterRequest.getPhone()));
        client.setEmail(clientRegisterRequest.getEmail());
        client.setRole(Role.ROLE_CLIENT);
        client.setEnabled(true);
        client.setMailConfirmed(false);
        client.setPhoneConfirmed(true);
        client.setLanguage(clientRegisterRequest.getLanguage());
        ClientNotificationSetting clientNotificationSetting= new ClientNotificationSetting();
        clientNotificationSetting.setStatusChangeAdvertisementMobileSending(Boolean.FALSE);
        clientNotificationSetting.setStatusChangeAdvertisementEmailSending(Boolean.FALSE);
        clientNotificationSetting.setNewAdvertisementBidMobileSending(Boolean.FALSE);
        clientNotificationSetting.setNewAdvertisementBidEmailSending(Boolean.FALSE);
        ClientAccountSetting clientAccountSetting= new ClientAccountSetting();
        clientAccountSetting.setCreateAdvertisementStartingAddressAutoFill(Boolean.FALSE);
        client.setAccountSetting(clientAccountSetting);
        client.setNotificationSetting(clientNotificationSetting);
        client.setPassword(bCryptPasswordEncoder.encode(clientRegisterRequest.getPassword()));
        client.setCitizenId(clientRegisterRequest.getCitizenId());

        if (clientRegisterRequest.getPersonType().equals("individual")) {
            client.setClientType(ClientType.PERSON);
        }

        //RegistrationRequestDto'dan gelen personType göre kayıt oluyor.
        else if (clientRegisterRequest.getPersonType().equals("corporate")) {

            checkedCorporatePersonNullCompanyData(clientRegisterRequest.getCommercialTitle(),clientRegisterRequest.getTaxAdministration(),clientRegisterRequest.getTaxNumber());

            CompanyValidatorDto companyValidatorDto = new CompanyValidatorDto(clientRegisterRequest.getTaxAdministration(), clientRegisterRequest.getTaxNumber());

            duplicateCompanyValidator.validate(companyValidatorDto);

            Company company = new Company();
            Address companyAddress = createAddressForCompany(clientRegisterRequest.getAddress().getFullAddress());

            company.setCommercialTitle(clientRegisterRequest.getCommercialTitle());
            company.setTaxAdministration(clientRegisterRequest.getTaxAdministration());
            company.setTaxNumber(clientRegisterRequest.getTaxNumber());
            company.setAddress(companyAddress);
            companyService.saveRepo(company);
            client.setCompany(company);
            client.setClientType(ClientType.CORPORATE);

        } else {
            throw new WrongPersonTypeException(Translator.toLocale("lojister.account.WrongPersonTypeException"));
        }

        client = clientService.saveRepo(client);

        return clientMapper.entityToDto(client);

    }

    @Override
    public DriverDto registerDriverWeb(RegistrationRequestDto registrationRequestDto) {

        duplicatePhoneValidator.validate(registrationRequestDto.getPhone());
        duplicateEmailValidator.validate(registrationRequestDto.getEmail());

        Driver driver = new Driver();

        driver.setFirstName(registrationRequestDto.getFirstName());
        driver.setLastName(registrationRequestDto.getLastName());
        driver.setEmail(registrationRequestDto.getEmail());

        driver.setPhone(phoneFormatter.format(registrationRequestDto.getPhone()));

        driver.setStatus(DriverStatus.REGISTERED);
        driver.setRole(Role.ROLE_DRIVER);
        driver.setDriverTitle(DriverTitle.BOSS);
        driver.setEnabled(true);
        driver.setMailConfirmed(false);
        driver.setPhoneConfirmed(true);
        driver.setLanguage(registrationRequestDto.getLanguage());
        DriverNotificationSetting driverNotificationSetting= new DriverNotificationSetting();
        driverNotificationSetting.setNewAdvertisementMailSending(Boolean.FALSE);
        driverNotificationSetting.setNewAdvertisementMobileSending(Boolean.FALSE);
        driverNotificationSetting.setStatusChangeAdvertisementEmailSending(Boolean.FALSE);
        driverNotificationSetting.setStatusChangeAdvertisementMobileSending(Boolean.FALSE);
        DriverAccountSetting driverAccountSetting= new DriverAccountSetting();
        driver.setNotificationSetting(driverNotificationSetting);

        driver.setPassword(bCryptPasswordEncoder.encode(registrationRequestDto.getPassword()));


        //RegistrationRequestDto'dan gelen personType göre kayıt oluyor.
        if (registrationRequestDto.getPersonType().equals("corporate")) {

            checkedCorporatePersonNullCompanyData(registrationRequestDto.getCommercialTitle(),registrationRequestDto.getTaxAdministration(),registrationRequestDto.getTaxNumber());

            CompanyValidatorDto companyValidatorDto = new CompanyValidatorDto(registrationRequestDto.getTaxAdministration(), registrationRequestDto.getTaxNumber());

            duplicateCompanyValidator.validate(companyValidatorDto);

            Company company = new Company();
            Address address = createAddressForCompany(registrationRequestDto.getAddress().getFullAddress());

            company.setCommercialTitle(registrationRequestDto.getCommercialTitle());
            company.setTaxNumber(registrationRequestDto.getTaxNumber());
            company.setTaxAdministration(registrationRequestDto.getTaxAdministration());
            company.setAddress(address);

            BankInformation bankInformation = createBankInformationForCompany();
            company.setBankInformation(bankInformation);
            company = companyService.saveRepo(company);
            driver.setCompany(company);
            driver = driverService.saveRepo(driver);

            return driverMapper.entityToDto(driver);

        } else {

            throw new WrongPersonTypeException(Translator.toLocale("lojister.account.WrongPersonTypeException"));
        }

    }


    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void verificationTokenIsExistCheckByUser(User user){

        Optional<VerificationToken> verificationTokenDb = verificationTokenService.findByUserIdOptional(user.getId());

        if (verificationTokenDb.isPresent()) {

            verificationTokenService.delete(verificationTokenDb.get());
        }
    }


    @Override
    public Boolean forgotMyPasswordMail(String mail, String siteURL) throws MessagingException, UnsupportedEncodingException {

        Optional<User> user = userService.findByEmailRepo(mail);

        if (user.isPresent()) {

            verificationTokenIsExistCheckByUser(user.get());

            VerificationToken verificationToken = new VerificationToken(user.get());
            verificationTokenService.saveRepo(verificationToken);
            ForgotMyPasswordMailDto forgotMyPasswordMailDto = new ForgotMyPasswordMailDto();
            forgotMyPasswordMailDto.setEmail(verificationToken.getUser().getEmail());
            forgotMyPasswordMailDto.setFirstName(verificationToken.getUser().getFirstName());
            forgotMyPasswordMailDto.setLastName(verificationToken.getUser().getLastName());
            forgotMyPasswordMailDto.setVerificationToken(verificationToken.getVerificationToken());
            forgotMyPasswordMailDto.setSiteURL(siteURL);

            forgotPasswordEmailSender.send(forgotMyPasswordMailDto);
        }
        return true;
    }

    @Override
    public Boolean resetPassword(ResetPasswordDto resetPasswordDto) {

        VerificationToken verificationTokenData = verificationTokenService.findDataByVerificationToken(resetPasswordDto.getVerificationToken());

        verificationTokenExpiredCheck(verificationTokenData);

        User user = verificationTokenData.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getNwPassword()));
        userService.saveRepo(user);
        verificationTokenService.delete(verificationTokenData);
        return true;
    }


    //TODO firebaseToken null yapılması sonradan sıkıntı çıkarır mı iyi analiz et.
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            User currentUser = securityContextUtil.getCurrentUser();
            currentUser.setFirebaseToken(null);
            userService.saveRepo(currentUser);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @Override
    public void checkedCorporatePersonNullCompanyData(String commercialTitle,String taxAdministration,String taxNumber) {

        if (StringUtils.isBlank(commercialTitle)) {
            throw new EmptyStringException(Translator.toLocale("lojister.account.EmptyStringException.company"));
        }

        if (StringUtils.isBlank(taxAdministration)) {
            throw new EmptyStringException(Translator.toLocale("lojister.account.EmptyStringException.taxAd"));
        }

        if (StringUtils.isBlank(taxNumber)) {
            throw new EmptyStringException(Translator.toLocale("lojister.account.EmptyStringException.taxNum"));
        }

    }


    @Override
    public Address createAddressForCompany(String fullAddress) {

        Address address = new Address();
        address.setCountry("");
        address.setDistrict("");
        address.setProvince("");
        address.setNeighborhood("");
        address.setFullAddress(fullAddress);

        return address;
    }

    @Override
    public BankInformation createBankInformationForCompany() {

        BankInformation bankInformation = new BankInformation();

        return bankInformationService.saveRepo(bankInformation);
    }

    @Override
    public void verificationTokenExpiredCheck(VerificationToken verificationToken) {

        if ((verificationTokenService.isExpiredToken(verificationToken.getId()))) {

            //Todo ingilizce
            throw new ExpiredVerificationTokenException(Translator.toLocale("lojister.account.ExpiredVerificationTokenException"));

        }

    }


}
