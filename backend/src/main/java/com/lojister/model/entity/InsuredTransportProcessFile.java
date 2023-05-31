package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.sql.Blob;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsuredTransportProcessFile extends AbstractTimestampEntity {

    @OneToOne
    private InsuredTransportProcess insuredTransportProcess;

    private String fileName;

    @Lob
    public Blob data;

}
