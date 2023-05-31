package com.lojister.model.dto.driver;

import com.lojister.model.dto.base.BaseDto;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DriverUpdateDto extends BaseDto {

    @NotNull
    @Size(min = 2)
    private String firstName;

    @NotNull
    @Size(min = 2)
    private String lastName;

    @NotNull
    private String phone;

    @NotNull
    @Size(min = 11,max = 11)
    private String citizenId;

    @Email
    @NotNull
    private String email;

    private String token;

}
