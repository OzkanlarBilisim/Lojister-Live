package com.lojister.model.dto;

import com.lojister.model.enums.ClientType;
import com.lojister.model.enums.DriverStatus;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.enums.Role;
import lombok.Data;

@Data
public class CurrentUserDto {

    private Long id;

    private Boolean current;
    private Double usd;
    private Double tl;
    private Double euro;


    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private Boolean phoneConfirmed;

    private Boolean mailConfirmed;

    private Role role;

    private DriverTitle driverTitle;

    private DriverStatus driverStatus;

    private String statusDescription;

    private Long companyId;

    private String companyCommercialTitle;

    private Double commissionRate;

    private ClientType clientType;

}
