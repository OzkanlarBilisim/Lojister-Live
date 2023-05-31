package com.lojister.model.entity;

import com.lojister.model.enums.InsuranceType;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.enums.TransportProcessType;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.Data;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class TransportProcess extends AbstractTimestampEntity {

    @Enumerated(EnumType.STRING)
    private TransportProcessStatus transportProcessStatus;

    @Enumerated(EnumType.STRING)
    private TransportProcessType transportProcessType;

    @Column(unique = true)
    private String transportCode;

    //Aracın içindeki driver silinir veya değiştirilirse bilgilerini tutmak için.
    @Embedded
    private SummaryDriverData summaryDriverData;

    //Araç silinir veya değiştirilirse bilgilerini tutmak için.
    @Embedded
    private SummaryVehicleData summaryVehicleData;

    private String driverExplanation;

    private String clientExplanation;

    private Double price;

    private Double priceWithVat;

    private String receiveQrCode;

    private String deliverQrCode;

    private LocalDate admissionDate;

    private LocalDate deliveryDate;

    private LocalTime admissionTime = LocalTime.of(0,0,0);

    private LocalTime deliveryTime =LocalTime.of(0,0,0);

    @Enumerated(EnumType.STRING)
    private InsuranceType insuranceType;




}
