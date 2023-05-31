package com.lojister.model.entity.driver;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.adminpanel.DriverDocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.sql.Blob;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDocumentFile extends AbstractTimestampEntity {

    @ManyToOne
    private DriverDocumentType driverDocumentType;

    private String fileName;

    private String contentType;

    @ManyToOne
    private Driver driver;

    @Lob
    private Blob data;

}
