package com.lojister.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class VehicleAndDocumentListDto {

    private VehicleDto vehicleDto;

    private List<VehicleDocumentMinimalDto> documentMinimalDtoList;

}
