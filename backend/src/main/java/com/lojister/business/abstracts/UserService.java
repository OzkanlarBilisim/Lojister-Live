package com.lojister.business.abstracts;

import com.lojister.model.dto.CurrentUserDto;
import com.lojister.controller.account.RegistrationRequestDto;
import com.lojister.model.dto.client.ClientDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.entity.User;
import com.lojister.model.entity.VerificationToken;
import com.lojister.model.enums.Language;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    DriverDto registerDriver(RegistrationRequestDto registrationRequestDto, String siteURL) throws UnsupportedEncodingException, MessagingException;

    ClientDto registerClient(RegistrationRequestDto registrationRequestDto, String siteURL) throws UnsupportedEncodingException, MessagingException;

    void sendVerificationEmail(VerificationToken verificationToken, String siteURL) throws MessagingException, UnsupportedEncodingException;

    Boolean verify(String verificationCode);

    CurrentUserDto getMe();

    Boolean updateFirebaseToken(String firebaseToken);

    void userEnabledCheck(User user);

    void userDriverRoleStatusEnabledCheck(User user);

    User findDataById(Long id);

    User saveRepo(User user);

    List<User> userDrivers();
    Optional<User> findByEmailRepo(String email);
    Boolean phoneVerification(String phone);
    Boolean emailVerification(String email);

    void changeLanguage(Language language);
}
