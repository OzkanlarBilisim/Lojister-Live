package com.lojister.service.PaymentService;

import com.lojister.model.dto.CardInformationsDto;
import com.lojister.model.dto.CardNameAndNumberDto;
import com.lojister.model.dto.SelectedCardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PaymentServiceNeccesaries {


    public String sendSelectedCardToMoka(SelectedCardDto selectedCardDto, HttpServletResponse response) throws Exception;

    String payBridge(CardInformationsDto cardInformationsDto, HttpServletResponse response) throws Exception;

    String successPayment(Model model, String paymentID) throws Exception;

    String failPayment(Model model, String paymentID, String resultCode) throws Exception;
    void afterPay(String advertID, String insuranceType);
    String getAfterPay(String advertID);
   List<CardNameAndNumberDto> getCardDataByUserId();

}
