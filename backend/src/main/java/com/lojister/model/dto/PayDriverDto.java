package com.lojister.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayDriverDto {

    @NotNull
    private Long transportPaymentId;

    private String paymentDescription;

    @NotNull
    private String paymentTransactionNumber;

    @NotNull
    private Boolean isPayment;

    private LocalDate paymentDate;

    private LocalTime paymentTime;

}
