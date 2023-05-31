package com.lojister.business.abstracts;

import com.lojister.model.dto.PayDriverDto;
import com.lojister.model.dto.TransportPaymentDetailDto;
import com.lojister.model.dto.TransportPaymentDto;
import com.lojister.model.entity.TransportPayment;

import java.util.List;

public interface TransportPaymentService {

    TransportPaymentDto getByTransportProcessId(Long transportProcessId);

    void payTheDriver(PayDriverDto payDriverDto);

    List<TransportPaymentDto> getAll();

    List<TransportPaymentDto> getPaymentWaiting();

    List<TransportPaymentDto> getPaymentCompleted();

    TransportPaymentDetailDto getTransportProcessDetailByTransportCodeForTransportPayment(String transportCode);

    TransportPayment saveRepo(TransportPayment transportPayment);

}
