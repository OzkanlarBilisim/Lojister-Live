package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.sql.Blob;

@Entity
@Data
public class ProfilePhotoFile extends AbstractTimestampEntity {

    @OneToOne
    private User user;

    private String fileName;

    private String path;

    @Lob
    public Blob data;

}
