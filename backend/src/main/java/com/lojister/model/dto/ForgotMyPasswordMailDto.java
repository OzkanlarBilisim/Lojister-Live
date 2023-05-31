package com.lojister.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgotMyPasswordMailDto {

    private String email;
    private String firstName;
    private String lastName;
    private String verificationToken;
    private String siteURL;

}
