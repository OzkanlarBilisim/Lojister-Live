package com.lojister.controller.transport;

import com.lojister.model.entity.User;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.model.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.sql.Blob;

@Getter
@Setter
public class DocumentResponse {
    private Long id;

    private String fileName;

    private String contentType;

    private Long userId;

    private String url;

    private DocumentType documentType;


}
