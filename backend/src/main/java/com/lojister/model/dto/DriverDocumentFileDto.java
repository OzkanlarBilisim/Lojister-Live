package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.dto.dynamic.DriverDocumentTypeDto;
import lombok.Data;

import javax.persistence.Lob;
import java.sql.Blob;

@Data
public class DriverDocumentFileDto extends BaseDto {

    private String fileName;

    private String contentType;

    private DriverDocumentTypeDto driverDocumentType;

    private DriverDto driver;

    private String path;

    @Lob
    private Blob data;

}
