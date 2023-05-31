package com.lojister.model.dto;

import com.lojister.model.entity.adminpanel.DriverDocumentType;
import lombok.Data;

@Data
public class DriverDocumentMinimalDto {

    private Long documentId;

    private DriverDocumentType driverDocumentType;

    private String fileName;

    public DriverDocumentMinimalDto(Long documentId, DriverDocumentType driverDocumentType, String fileName) {
        this.documentId = documentId;
        this.driverDocumentType = driverDocumentType;
        this.fileName = fileName;
    }
}
