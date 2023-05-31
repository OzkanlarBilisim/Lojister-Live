package com.lojister.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ContactMailDto {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Email(message = "{lojister.constraint.register.email.Email.message}")
    private String email;

    @NotNull
    @NotBlank
    private String message;

}
