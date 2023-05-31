package com.lojister.model.dto;

import com.lojister.model.enums.DocumentType;
import java.sql.Blob;

public class DocumentFileDto {

    private Blob data;
    DocumentType documentType;
}
