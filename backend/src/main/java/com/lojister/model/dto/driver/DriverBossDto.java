package com.lojister.model.dto.driver;

import com.lojister.model.dto.CompanyDto;
import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.enums.DriverStatus;
import com.lojister.model.entity.Position;
import com.lojister.model.enums.Role;
import lombok.Data;

@Data
public class DriverBossDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private Boolean phoneConfirmed;

    private Boolean mailConfirmed;

    private Boolean enabled;

    private Role role;

    private DriverStatus status;

    private String citizenId;

    private CompanyDto company;

    private Float rating;

    private Position lastPosition;
}
