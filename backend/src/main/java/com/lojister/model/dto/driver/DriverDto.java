package com.lojister.model.dto.driver;

import com.lojister.model.dto.CompanyDto;
import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.entity.Position;
import com.lojister.model.enums.DriverStatus;
import lombok.Data;

@Data
public class DriverDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private DriverStatus status;

    private String statusDescription;

    private String citizenId;

    private CompanyDto company;

    private DriverBossDto boss;

    private Float rating;

    private Position lastPosition;

}
