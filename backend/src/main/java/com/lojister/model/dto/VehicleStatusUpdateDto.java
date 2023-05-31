package com.lojister.model.dto;

import lombok.Data;

@Data
public class VehicleStatusUpdateDto {

    private Boolean value;
    private String statusDescription;
    private Long vehicleId;

}
