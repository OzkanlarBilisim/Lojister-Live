package com.lojister.business.abstracts;


import com.lojister.model.dto.DocumentFileDto;
import com.lojister.model.dto.DocumentFileWithoutDataDto;
import com.lojister.model.entity.DocumentFile;
import com.lojister.model.enums.DocumentType;

import java.sql.Blob;
import java.util.List;

public interface DocumentService {

    DocumentFile findTransportProcessIdAndUserId(Long userId,Long clientTransportProcessId);
    void save(DocumentFile documentFile);
    Blob getData(Long id);
    Boolean isDocumentType(Long clientTransportProcessId, DocumentType documentType);
    List<DocumentFileWithoutDataDto> findTransportProcessId(Long clientTransportProcessId);
}
