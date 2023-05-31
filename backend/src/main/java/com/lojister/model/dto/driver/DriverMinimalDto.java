package com.lojister.model.dto.driver;

import com.lojister.model.enums.DriverStatus;
import lombok.Data;

@Data
public class DriverMinimalDto {

    private String companyName;

    private Long companyId;

    private DriverStatus driverStatus;

    private Long driverId;

    private String driverFirstName;

    private String driverLastName;

    private String statusDescription;

}
