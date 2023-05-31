package com.lojister.model.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class EmailDto {

    @NotNull
    @Email
    private String email;

}
