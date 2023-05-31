package com.lojister.service.PaymentService;

import com.lojister.model.dto.CurrentPayDataDto;
import com.lojister.model.dto.CurrentPaymentRequestsDto;

import java.util.List;

public interface CurrentPaymentService {
    String pay(CurrentPayDataDto data) throws Exception;

    List<CurrentPaymentRequestsDto> CurrentRequests();
}
