package com.lojister.model.dto;

import com.lojister.model.dto.driver.DriverMinimalDto;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.entity.adminpanel.DriverDocumentType;
import lombok.Data;


@Data
public class DriverDocumentNotDataDto {

    private Long documentId;

    private DriverDocumentType driverDocumentType;

    private DriverMinimalDto driverDto;

    private String fileName;

    public DriverDocumentNotDataDto(Long documentId, DriverDocumentType driverDocumentType, Driver driver, String fileName) {

        DriverMinimalDto driverMinimalDto = new DriverMinimalDto();
        driverMinimalDto.setDriverStatus(driver.getStatus());
        driverMinimalDto.setDriverFirstName(driver.getFirstName());
        driverMinimalDto.setDriverLastName(driver.getLastName());
        driverMinimalDto.setCompanyId(driver.getCompany().getId());
        driverMinimalDto.setCompanyName(driver.getCompany().getCommercialTitle());
        driverMinimalDto.setDriverId(driver.getId());
        driverMinimalDto.setStatusDescription(driver.getStatusDescription());

        this.documentId = documentId;
        this.driverDocumentType = driverDocumentType;
        this.driverDto = driverMinimalDto;
        this.fileName = fileName;

    }

}
