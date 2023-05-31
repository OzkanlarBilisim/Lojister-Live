package com.lojister.business.abstracts;

import com.lojister.model.enums.InsuranceType;
import org.springframework.ui.Model;

import java.security.NoSuchAlgorithmException;

public interface PaymentService {

    String paymentOperation(Model model, String transportCode, InsuranceType insuranceType) throws NoSuchAlgorithmException;
    String paymentOperationAbroud(Model model, int transportCode, String insuranceType) throws NoSuchAlgorithmException;
    String successPayment(Model model, String transportCode);
    String successPaymentAbroud(Model model, int transportCode);

    String failPayment(Model model, String transportCode);

}
