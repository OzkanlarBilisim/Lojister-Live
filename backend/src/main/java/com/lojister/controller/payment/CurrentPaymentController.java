package com.lojister.controller.payment;

import com.lojister.model.dto.CurrentPayDataDto;
import com.lojister.model.dto.CurrentPaymentRequestsDto;
import com.lojister.service.PaymentService.CurrentPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currentPayment")
public class CurrentPaymentController {
    @Autowired
    private CurrentPaymentService currentPaymentService;


    @PostMapping("/pay")
    public String pay(@RequestBody CurrentPayDataDto data) throws Exception {
        return currentPaymentService.pay(data);
    }
    @GetMapping("/requests")
    public List<CurrentPaymentRequestsDto> getCurrentRequests() {
        return currentPaymentService.CurrentRequests();
    }
}
