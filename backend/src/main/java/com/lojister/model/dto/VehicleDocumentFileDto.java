package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.dynamic.VehicleDocumentTypeDto;
import lombok.Data;

import javax.persistence.Lob;
import java.sql.Blob;

@Data
public class VehicleDocumentFileDto extends BaseDto {

    private VehicleDto vehicle;

    private VehicleDocumentTypeDto vehicleDocumentType;

    private String fileName;

    private String path;

    @Lob
    public Blob data;


}
