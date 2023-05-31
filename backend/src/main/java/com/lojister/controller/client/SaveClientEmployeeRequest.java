package com.lojister.controller.client;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SaveClientEmployeeRequest {
    @NotNull(message = "{lojister.constraint.register.firstName.NotNull.message}")
    @Size(min = 2,message = "lojister.constraint.register.firstName.Size.message")
    private String firstName;
    @NotNull(message = "{lojister.constraint.register.lastName.NotNull.message}")
    @Size(min = 2,message = "{lojister.constraint.register.lastName.Size.message}")
    private String lastName;
    @NotNull(message = "{lojister.constraint.register.password.NotNull.message}")
    @Size(min = 8,max = 32,message = "{lojister.constraint.register.password.Size.message}")
    private String password;
    @NotNull(message = "{lojister.constraint.register.phone.NotNull.message}")
    @Size(min =13,max = 20,message = "{lojister.constraint.register.phone.Size.message}")
    private String phone;
    @NotNull(message = "{lojister.constraint.register.email.NotNull.message}")
    @Email(message = "{lojister.constraint.register.email.Email.message}")
    private String email;
}
