package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.adminpanel.VehicleDocumentType;
import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Data
public class VehicleDocumentFile extends AbstractTimestampEntity {

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn()
    private VehicleDocumentType vehicleDocumentType;

    private String fileName;

    private String path;

    @Lob
    public Blob data;

}
