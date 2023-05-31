package com.lojister.model.dto;

import com.lojister.model.dto.driver.DriverDto;
import lombok.Data;

import java.util.List;

@Data
public class DriverAndDocumentListDto {

    private DriverDto driverDto;

    private List<DriverDocumentMinimalDto> documentMinimalDtoList;

}
