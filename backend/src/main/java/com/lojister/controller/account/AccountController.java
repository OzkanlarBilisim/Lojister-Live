package com.lojister.controller.account;

import com.lojister.controller.abroudControler.AbroudBidController;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.CurrentUserDto;
import com.lojister.model.dto.FirebaseTokenDto;
import com.lojister.model.dto.TokenResponseDto;
import com.lojister.model.dto.client.ClientDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.dto.request.EmailDto;
import com.lojister.model.dto.request.ResetPasswordDto;
import com.lojister.business.concretes.UserServiceImpl;
import com.lojister.business.abstracts.AccountService;
import com.lojister.model.enums.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.Cookie;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/account")
@CrossOrigin
@Authenticated
public class AccountController {

    @Autowired
    private AbroudBidController abroudBidController;
    private final UserServiceImpl userServiceImpl;
    private final AccountService accountService;

    @Autowired
    public AccountController(UserServiceImpl userServiceImpl,
                             AccountService accountService) {

        this.userServiceImpl = userServiceImpl;
        this.accountService = accountService;
    }


    @GetMapping("/user/me")
    @ResponseBody
    public ResponseEntity<CurrentUserDto> getMe() {
        return ResponseEntity.ok(userServiceImpl.getMe());
    }

    @PostMapping("/login")
    @ResponseBody
    @PermitAllCustom
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(accountService.login(request));
    }



    @PostMapping("/register/client/web")
    @ResponseBody
    @PermitAllCustom
    public ResponseEntity<ClientDto> registerClientWeb(@Valid @RequestBody ClientRegisterRequest clientRegisterRequest) {
        ClientDto clientDto = accountService.registerClientWeb(clientRegisterRequest);
        return ResponseEntity.ok(clientDto);
    }

    @PostMapping("/register/driver/web")
    @ResponseBody
    @PermitAllCustom
    public ResponseEntity<DriverDto> registerDriverWeb(@Valid @RequestBody RegistrationRequestDto registrationRequest) {
        DriverDto driverDto = accountService.registerDriverWeb(registrationRequest);

        return ResponseEntity.ok(driverDto);
    }


    @PostMapping("/register/client")
    @PermitAllCustom
    public String registerClient(@Valid @RequestBody RegistrationRequestDto registrationRequest, HttpServletRequest request) throws AuthenticationException, UnsupportedEncodingException, MessagingException {
        ClientDto clientDto = userServiceImpl.registerClient(registrationRequest, getSiteURL(request));
        return "register_success";
    }

    @PostMapping("/register/driver")
    @PermitAllCustom
    public String registerDriver(@Valid @RequestBody RegistrationRequestDto registrationRequest, HttpServletRequest request) throws AuthenticationException, UnsupportedEncodingException, MessagingException {
        DriverDto driverDto = userServiceImpl.registerDriver(registrationRequest, getSiteURL(request));
        return "register_success";
    }

    @ResponseBody
    @PermitAllCustom
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }


    @GetMapping("/verify")
    @PermitAllCustom
    public String verifyUser(@RequestParam("code") String verificationCode) {
        if (userServiceImpl.verify(verificationCode)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        accountService.logout(request, response);

    }


    @PostMapping("/resetPasswordMail")
    @ResponseBody
    @PermitAllCustom
    public Boolean resetPasswordMail(@Valid @RequestBody EmailDto emailDto, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {

        return accountService.forgotMyPasswordMail(emailDto.getEmail(), getSiteURL(request));

    }

    @PostMapping("/resetPassword")
    @ResponseBody
    @PermitAllCustom
    public Boolean resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {

        return accountService.resetPassword(resetPasswordDto);

    }

    @PutMapping("/firebaseToken")
    @ResponseBody
    public Boolean updateFirebaseToken(@RequestBody FirebaseTokenDto firebaseTokenDto) {

        return userServiceImpl.updateFirebaseToken(firebaseTokenDto.getFirebaseToken());

    }
    @PermitAllCustom
    @GetMapping("/phone/verification")
    public @ResponseBody Boolean phoneVerification(@RequestParam String phone){
       return userServiceImpl.phoneVerification(phone);
    }
    @PermitAllCustom
    @GetMapping("/email/verification")
    public @ResponseBody Boolean emailVerification(@RequestParam String email){
      return userServiceImpl.emailVerification(email);
    }

    @PutMapping("/changeLanguage")
    public @ResponseBody void changeLanguage(@RequestParam Language language){
        userServiceImpl.changeLanguage(language);
    }

}
