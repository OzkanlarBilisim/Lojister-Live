package com.lojister.controller.payment;

import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.CardInformationsDto;
import com.lojister.model.dto.CardNameAndNumberDto;
import com.lojister.model.dto.SelectedCardDto;
import com.lojister.model.entity.payment.RegisteredCards;
import com.lojister.repository.payment.RegisteredCardsRepository;
import com.lojister.service.PaymentService.PaymentServiceNeccesaries;
import com.lojister.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/paymentMethod")
@RestController
public class PaymentServiceController {
    @Autowired
    private PaymentServiceNeccesaries paymentServiceNeccesaries;
    @Autowired
    private SecurityContextUtil securityContextUtil;
    @Autowired
    private RegisteredCardsRepository registeredCardsRepository;

    @PostMapping("/card-info")
    public ResponseEntity<String> createCardInformations(@RequestBody CardInformationsDto cardInformationsDto) {

        return new ResponseEntity<>("Kart Bilgileri Gönderildi", HttpStatus.CREATED);
    }

    @PostMapping("/3d")
    public String processPayment (@RequestBody CardInformationsDto cardInformationsDto, HttpServletResponse response) throws Exception {
        return paymentServiceNeccesaries.payBridge(cardInformationsDto, response);
    }



    @GetMapping("/cards")
    public List<CardNameAndNumberDto> getCardDataByUserId(){
        return paymentServiceNeccesaries.getCardDataByUserId();
    }

    @PostMapping("/selected-card")
    public String sendSelectedCardToMoka(@RequestBody SelectedCardDto selectedCard, HttpServletResponse response) throws Exception {
        return paymentServiceNeccesaries.sendSelectedCardToMoka(selectedCard, response);
    }
    @PostMapping("/afterPay/{advertID}/{insuranceType}")
    public void afterPay(@PathVariable("advertID") String advertID, @PathVariable("insuranceType") String insuranceType){
        paymentServiceNeccesaries.afterPay(advertID, insuranceType);
    }
    @GetMapping("/getAfterPay/{advertID}")
    public String getAfterPay(@PathVariable("advertID") String advertID){
        return paymentServiceNeccesaries.getAfterPay(advertID);
    }


}