package com.lojister.model.dto.driver;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DriverEmployeeSaveDto extends AbstractTimestampEntity {

    @NotNull
    @Size(min = 2,max = 50)
    private String firstName;

    @NotNull
    @Size(min = 8,max = 32)
    private String password;

    @NotNull
    @Size(min = 2,max = 40)
    private String lastName;

    @NotNull
    @Size(min = 10,max = 20)
    private String phone;

    @Email
    private String email;

    @NotNull
    @Size(min = 11, max = 11)
    private String citizenId;

}
