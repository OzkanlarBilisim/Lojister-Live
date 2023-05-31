package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.enums.DocumentType;
import lombok.*;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentFile extends AbstractTimestampEntity {

    private String fileName;

    private String contentType;

    @ManyToOne
    private ClientTransportProcess clientTransportProcess;

    @ManyToOne
    private User user;

    @Lob
    private Blob data;

    @Enumerated(EnumType.STRING)
    DocumentType documentType;
}
