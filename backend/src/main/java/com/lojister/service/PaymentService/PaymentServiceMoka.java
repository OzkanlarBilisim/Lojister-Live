package com.lojister.service.PaymentService;

import com.lojister.model.dto.CardInformationsDto;

public interface PaymentServiceMoka {
    String processPayment(CardInformationsDto cardInformationsDto, String totalPrice, String referenceCode, String paymentID, String URL);
}
