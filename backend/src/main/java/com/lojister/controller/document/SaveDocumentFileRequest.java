package com.lojister.controller.document;

import com.lojister.model.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Getter
@Setter
public class SaveDocumentFileRequest {

    private String fileName;
    DocumentType documentType;
}
