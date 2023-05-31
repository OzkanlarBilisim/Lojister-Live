package com.lojister.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {

    @NotNull
    private String verificationToken;

    @NotNull
    @Size(min = 8,max = 32)
    private String nwPassword;

}
