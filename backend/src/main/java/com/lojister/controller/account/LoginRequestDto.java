package com.lojister.controller.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotNull(message = "{lojister.constraint.login.phoneNumber.NotNull.message}")
    @Size(min = 10,max = 20,message = "{lojister.constraint.login.phoneNumber.Size.message}")
    private String phoneNumber;

    @NotNull(message = "{lojister.constraint.login.password.NotNull.message}")
    @Size(min = 8,max = 32,message = "{lojister.constraint.login.password.Size.message}")
    private String password;

    private Boolean rememberMe;

}
