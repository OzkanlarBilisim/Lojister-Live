package com.lojister.service.PaymentService;

import com.lojister.model.dto.CardInformationsDto;
import com.lojister.other.Other;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
@Service
public class PaymentServiceMokaImpl implements PaymentServiceMoka{
    private final RestTemplate restTemplate;


    public PaymentServiceMokaImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Autowired
    private PaymentServiceNeccesariesImpl paymentServiceNeccesaries;
    @Autowired
    private Other other;

    @Value("${lojister.moka.url}")
    private String MOKA;
    @Value("${lojister.environment}")
    private String ENVIRONMENT;


    public String processPayment(CardInformationsDto cardInformationsDto, String totalPrice, String referenceCode, String paymentID, String URL) {


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ipAddress = request.getRemoteAddr();



        // PaymentDealerAuthentication verilerini içeren bir Map oluştur
        Map<String, String> paymentDealerAuthentication = new LinkedHashMap<>();
        if (ENVIRONMENT.equals("prod")){
            paymentDealerAuthentication.put("DealerCode", "44755");
            paymentDealerAuthentication.put("Username", "dcf131c3-745a-455f-a09c-034586dee57b");
            paymentDealerAuthentication.put("Password", "390cacd8-1cf1-4dbc-88e3-b1d36c2e6dfe");
            paymentDealerAuthentication.put("CheckKey", "8ad836af2f59c9c6580c1da4c83314f288875691241e5dac7a39c509be565d51");
        }

// PaymentDealerRequest verilerini içeren bir Map oluştur
        Map<String, String> paymentDealerRequest = new LinkedHashMap<>();
        paymentDealerRequest.put("CardHolderFullName", cardInformationsDto.getCardHolderFullName());
        paymentDealerRequest.put("CardNumber", cardInformationsDto.getCardNumber());
        paymentDealerRequest.put("ExpMonth", cardInformationsDto.getExpDates().getExpMonth());
        paymentDealerRequest.put("ExpYear",  cardInformationsDto.getExpDates().getExpYear());
        paymentDealerRequest.put("CvcNumber", cardInformationsDto.getCvcNumber());
        paymentDealerRequest.put("CardToken", "");
        paymentDealerRequest.put("Amount", totalPrice);
        paymentDealerRequest.put("Currency", "TL");
        paymentDealerRequest.put("InstallmentNumber", "1");
        paymentDealerRequest.put("ClientIP", ipAddress);
        paymentDealerRequest.put("OtherTrxCode", referenceCode);
        paymentDealerRequest.put("SubMerchantName", "");
        paymentDealerRequest.put("IsPoolPayment", "0");
        paymentDealerRequest.put("IsTokenized", "0");
        paymentDealerRequest.put("IntegratorId", "0");
        paymentDealerRequest.put("Software", "Lojister");
        paymentDealerRequest.put("Description", "");
        paymentDealerRequest.put("IsPreAuth", "0");
        paymentDealerRequest.put("ReturnHash", "1");
        paymentDealerRequest.put("RedirectUrl", URL);


// JSON verilerini içeren bir Map oluştur
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("PaymentDealerAuthentication", paymentDealerAuthentication);
        requestBody.put("PaymentDealerRequest", paymentDealerRequest);

        // İstek başlıklarını ve medya tipini ayarla
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity'yi oluştur
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        // İstek gönder
        ResponseEntity<String> response = restTemplate.postForEntity(MOKA, requestEntity, String.class);

        // Yanıtı al
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            System.out.println("İstek başarıyla gönderildi. Yanıt: " + responseBody);
        } else {
            System.out.println("İstek gönderilirken bir hata oluştu. Durum kodu: " + response.getStatusCodeValue());
        }
        return response.getBody();
    }


}

