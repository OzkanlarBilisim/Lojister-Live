package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.PaymentControl;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Service;

public interface PaymentControlService {
    public PaymentControl save(PaymentControl paymentControl);
    public void successPayment(int transportCode, String insuranceCost, String key);
    public void successPaymentDomestic(String transportCode, String key);
    public String addNewPayment(int transportCode);
    public String addNewPaymentDomestic(String transportCode);


}
