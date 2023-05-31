package com.lojister.model.dto;

import com.lojister.model.entity.adminpanel.VehicleDocumentType;
import lombok.Data;

@Data
public class VehicleDocumentMinimalDto {

    private Long documentId;

    private VehicleDocumentType vehicleDocumentType;

    private String fileName;

    public VehicleDocumentMinimalDto(Long documentId, VehicleDocumentType vehicleDocumentType, String fileName) {
        this.documentId = documentId;
        this.vehicleDocumentType = vehicleDocumentType;
        this.fileName = fileName;
    }
}
