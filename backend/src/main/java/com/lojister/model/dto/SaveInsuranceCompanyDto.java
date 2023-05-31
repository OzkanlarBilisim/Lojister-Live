package com.lojister.model.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SaveInsuranceCompanyDto {

    @NotNull(message = "{lojister.constraint.firstName.NotNull.message}")
    @Size(min = 2, max = 32, message = "{lojister.constraint.firstName.Size.message}")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",message = "{lojister.constraint.firstName.Pattern.message}")
    private String firstName;

    @NotNull(message = "{lojister.constraint.lastName.NotNull.message}")
    @Size(min = 2, max = 32, message = "{lojister.constraint.lastName.Size.message}")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",message = "{lojister.constraint.lastName.Pattern.message}")
    private String lastName;

    @NotNull(message = "{lojister.constraint.password.NotNull.message}")
    @Size(min = 6, max = 32, message = "{lojister.constraint.password.Size.message}")
    private String password;

    @NotNull(message = "{lojister.constraint.phone.NotBlank.message}")
    @Pattern(regexp = "^[+90][0-9]+$", message = "{lojister.constraint.phoneNumber.Pattern.message}")
    @Size(min = 13, max = 13, message = "{lojister.constraint.phoneNumber.Size.message}")
    private String phone;

    @Email
    @NotNull
    private String email;
}
