package com.lojister.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportPaymentDetailDto {

    private LocalDate admissionDate;

    private LocalDate deliveryDate;

    private String driverFirstname;

    private String driverLastname;

    private String driverNumber;

    private String bossFirstname;

    private String bossLastname;

    private String bossNumber;

    private String companyName;

    private String licencePlate;

    private String startAddress;

    private String finishAddress;

    private String clientFirstname;

    private String clientLastname;

    private String clientNumber;

    private String clientExplanation;

    private String startRecipientFirstname;

    private String startRecipientLastname;

    private String startRecipientNumber;

    private String dueRecipientFirstname;

    private String dueRecipientLastname;

    private String dueRecipientNumber;

    private LocalTime admissionTime ;

    private LocalTime deliveryTime ;

    private LocalDateTime advertisementStartDate;

    private LocalDateTime bidStartDate;

}
