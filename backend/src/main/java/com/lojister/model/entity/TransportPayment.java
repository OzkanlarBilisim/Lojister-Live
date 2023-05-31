package com.lojister.model.entity;

import com.lojister.model.enums.PaymentStatus;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportPayment extends AbstractTimestampEntity {

    @OneToOne
    private TransportProcess transportProcess;

    private Double price;

    private LocalDate paymentDate;

    private LocalTime paymentTime;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paymentTransactionNumber;

    private String paymentDescription;

}
