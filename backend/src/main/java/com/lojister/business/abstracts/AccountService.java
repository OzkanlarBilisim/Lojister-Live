package com.lojister.business.abstracts;


import com.lojister.controller.account.ClientRegisterRequest;
import com.lojister.controller.account.LoginRequestDto;
import com.lojister.controller.account.RegistrationRequestDto;
import com.lojister.model.dto.request.ResetPasswordDto;
import com.lojister.model.dto.TokenResponseDto;
import com.lojister.model.dto.client.ClientDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.entity.Address;
import com.lojister.model.entity.BankInformation;
import com.lojister.model.entity.VerificationToken;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface AccountService {

    TokenResponseDto login(LoginRequestDto request);

    ClientDto registerClientWeb(ClientRegisterRequest clientRegisterRequest);

    DriverDto registerDriverWeb(RegistrationRequestDto registrationRequest);

    Boolean forgotMyPasswordMail(String mail, String siteURL) throws MessagingException, UnsupportedEncodingException;

    Boolean resetPassword(ResetPasswordDto resetPasswordDto);

    void logout(HttpServletRequest request, HttpServletResponse response);

    void checkedCorporatePersonNullCompanyData(String commercialTitle,String taxAdministration,String taxNumber);

    Address createAddressForCompany(String fullAddress);

    BankInformation createBankInformationForCompany();

    void verificationTokenExpiredCheck(VerificationToken verificationToken);

}
