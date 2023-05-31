package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.enums.PaymentStatus;
import com.lojister.model.enums.TransportProcessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportPaymentDto extends BaseDto {

    private Long transportProcessId;

    private TransportProcessType transportProcessType;

    private Double price;

    private String transportCode;

    private String bankName;

    private String branch;

    private String accountNumber;

    private String iban;

    private LocalDate paymentDate;

    private LocalTime paymentTime;

    private PaymentStatus paymentStatus;

    private String paymentTransactionNumber;

    private String paymentDescription;

}
