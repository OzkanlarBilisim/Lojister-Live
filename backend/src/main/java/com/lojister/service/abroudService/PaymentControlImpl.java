package com.lojister.service.abroudService;


import com.lojister.core.exception.DriverStatusException;
import com.lojister.model.abroudModel.PaymentControl;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.other.Other;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.repository.abroudRepository.PaymentControlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaymentControlImpl implements PaymentControlService{
    @Autowired
    private PaymentControlRepository paymentControlRepository;
    @Autowired
    private AbroudService abroudService;
    @Autowired
    private AbroudRepository abroudRepository;
    @Autowired
    private AbroudBidRepository abroudBidRepository;

    @Autowired
    private Other other;
    @Autowired
    private ShipsmentInfoService shipsmentInfoService;


    @Override
    public PaymentControl save(PaymentControl paymentControl) {
        return paymentControlRepository.save(paymentControl);
    }

    @Override
    public String addNewPayment(int transportCode){
        String paymetKey = other.randomLetter(10)+"="+transportCode;
        PaymentControl paymentControl = new PaymentControl();

        paymentControl.setBankKey(paymetKey);
        paymentControl.setBankKeyStatus(true);
        paymentControl.setDateNow(new Date());
        paymentControl.setDomesticOrAbroad("abroad");
        //save(paymentControl);
        paymentControlRepository.save(paymentControl);

        return paymetKey;
    }
    @Override
    public String addNewPaymentDomestic(String transportCode){
        String paymetKey = other.randomLetter(10)+"="+transportCode;
        PaymentControl paymentControl = new PaymentControl();

        paymentControl.setBankKey(paymetKey);
        paymentControl.setBankKeyStatus(true);
        paymentControl.setDateNow(new Date());
        paymentControl.setDomesticOrAbroad("domestic");
        //save(paymentControl);
        paymentControlRepository.save(paymentControl);

        return paymetKey;
    }

    @Override
    public void successPayment(int transportCode, String insuranceCost, String key) {
        PaymentControl paymentControl = paymentControlRepository.keyFind(key);

        if(paymentControl.getBankKeyStatus()){

            List<abroudBid> abroudBid2 = abroudBidRepository.findbidPaymentSuccessAproved(transportCode);

            Boolean insuranceStatus = false;
            if (!insuranceCost.equals("0")){
                insuranceStatus = true;
            }

            paymentControl.setBankKeyStatus(false);
            paymentControl.setAbroudBid(abroudBid2.get(0));
            paymentControl.setInsuranceStatus(insuranceStatus);
            paymentControl.setInsuranceCost(Double.valueOf(insuranceCost));

            abroudService.advertStatusStep(transportCode);
            save(paymentControl);

        }
    }

    @Override
    public void successPaymentDomestic(String transportCode, String key) {
        String key2 = key;

        if(key.indexOf(",") != -1){
            key2 = key.split(",")[1];
        }
        PaymentControl paymentControl = paymentControlRepository.keyFind(key2);

        if(paymentControl.getBankKeyStatus()){


            Boolean insuranceStatus = false;
            paymentControl.setBankKeyStatus(false);
            paymentControl.setAbroudBid(null);
            paymentControl.setInsuranceStatus(insuranceStatus);
            paymentControl.setInsuranceCost(0.0);

            save(paymentControl);
        }else {
            throw new RuntimeException("Ödeme yapılamadı");
        }
    }
}
