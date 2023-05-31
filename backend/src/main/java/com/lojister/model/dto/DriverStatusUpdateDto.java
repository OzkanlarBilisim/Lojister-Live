package com.lojister.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DriverStatusUpdateDto {

    @NotNull
    private Boolean value;

    private String statusDescription;

    @NotNull
    private Long driverId;
}
