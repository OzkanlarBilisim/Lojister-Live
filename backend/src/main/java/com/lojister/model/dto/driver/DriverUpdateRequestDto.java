package com.lojister.model.dto.driver;

import com.lojister.model.dto.base.BaseDto;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DriverUpdateRequestDto extends BaseDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String phone;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 11,max = 11)
    private String citizenId;

}
