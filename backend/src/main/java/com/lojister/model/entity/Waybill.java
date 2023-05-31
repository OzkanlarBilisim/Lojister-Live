package com.lojister.model.entity;

import com.lojister.model.enums.WaybillStatus;
import com.lojister.model.entity.base.CreatedDateAndIdBaseEntity;
import com.lojister.model.entity.client.ClientTransportProcess;
import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Data
public class Waybill extends CreatedDateAndIdBaseEntity {

    @OneToOne
    private ClientTransportProcess clientTransportProcess;

    @Lob
    private Blob data;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private WaybillStatus waybillStatus;

}
