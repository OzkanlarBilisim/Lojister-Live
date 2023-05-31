package com.lojister.controller.payment;

import com.lojister.business.abstracts.MailNotificationService;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.service.PaymentService.PaymentServiceNeccesaries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/paymentResult")
@CrossOrigin
@Authenticated
public class PaymentResult {
    @Autowired
    private PaymentServiceNeccesaries paymentServiceNeccesaries;
    @Autowired
    private MailNotificationService mailNotificationService;

    @Value("${lojister.environment}")
    private String ENVIRONMENT;


    @PostMapping("/result")
    @PermitAllCustom
    public String successPaymentAbroud(Model model, @RequestParam("paymentID") String paymentID, @RequestParam("resultCode") String resultCode) throws Exception {
        if (resultCode.isEmpty()){
            return paymentServiceNeccesaries.successPayment(model, paymentID);
        }else {
            return paymentServiceNeccesaries.failPayment(model ,paymentID, resultCode);
        }
    }
    @GetMapping("/resultDev")
    @PermitAllCustom
    public String successPaymentAbroudDev(Model model, @RequestParam("paymentID") String paymentID, @RequestParam("resultCode") String resultCode) throws Exception {
       if (!ENVIRONMENT.equals("dev")){
           throw new Exception("Yetkiniz Yok");
       }

        if (resultCode.isEmpty()){
            return paymentServiceNeccesaries.successPayment(model, paymentID);
        }else {
            return paymentServiceNeccesaries.failPayment(model ,paymentID, resultCode);
        }
    }


}