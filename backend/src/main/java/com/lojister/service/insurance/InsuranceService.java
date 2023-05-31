package com.lojister.service.insurance;

import com.lojister.model.entity.payment.Insurance;

public interface InsuranceService {
    Insurance insurance(Double goodsPrice, String startCountryCode, String dueCountryoCde, String advertCurrencySymbol, String InsuranceType, String InsuranceCurencySymbol);

    String sendMail(String transportID, Insurance insuranceDto);
    Insurance save(Insurance insurance);
}
