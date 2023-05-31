package com.lojister.service.PaymentService;


import com.lojister.business.abstracts.ClientAdvertisementBidService;
import com.lojister.business.abstracts.ClientAdvertisementService;
import com.lojister.business.abstracts.ClientService;
import com.lojister.business.abstracts.TransportProcessService;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.dto.CardDatesDto;
import com.lojister.model.dto.CardInformationsDto;
import com.lojister.model.dto.CardNameAndNumberDto;
import com.lojister.model.dto.SelectedCardDto;
import com.lojister.model.entity.TransportProcess;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.payment.AfterPay;
import com.lojister.model.entity.payment.Insurance;
import com.lojister.model.entity.payment.Payment;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.entity.payment.RegisteredCards;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.repository.payment.AfterPayRepository;
import com.lojister.repository.payment.PaymentMokaRepository;
import com.lojister.repository.payment.RegisteredCardsRepository;
import com.lojister.service.abroudService.AbroudBidService;
import com.lojister.service.abroudService.AbroudService;
import com.lojister.service.api.TcmbApiService;
import com.lojister.service.insurance.InsuranceService;
import com.lojister.service.insurance.InsuranceServiceImpl;
import com.lojister.util.AESUtil;
import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import javax.transaction.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceNeccesariesImpl implements PaymentServiceNeccesaries{

    @Autowired
    private SecurityContextUtil securityContextUtil;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PaymentMokaRepository paymentMokaRepository;
    @Autowired
    private ClientAdvertisementService clientAdvertisementService;
    @Autowired
    private ClientAdvertisementBidService clientAdvertisementBidService;
    @Autowired
    private AbroudService abroudService;
    @Autowired
    private AbroudBidService abroudBidService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TcmbApiService tcmbApiService;
    @Autowired
    private PaymentServiceMoka paymentServiceMoka;
    @Value("${lojister.site.url}")
    private String Frontend;
    @Value("${lojister.payment.url}")
    private String URL;
    @Autowired
    private RegisteredCardsRepository registeredCardsRepository;
    @Autowired
    private AESUtil aesUtil;
    @Value("${lojister.environment}")
    private String ENVIRONMENT;
    @Autowired
    private AfterPayRepository afterPayRepository;
    @Autowired
    private TransportProcessService transportProcessService;


    ClientAdvertisement clientAdvertisament = null;
    public String payBridge(CardInformationsDto cardInformationsDto, HttpServletResponse response) throws Exception {
        if(cardInformationsDto.getAbroadOrDomestic().equals("Abroud")){
            cardInformationsDto.setInsuranceType(getAfterPay(cardInformationsDto.getTransportID()));
        }

        Payment payment = addPaymentTable(cardInformationsDto);
        String referenceCode = null;
        if (payment.getAbroadOrDomestic().equals("Domastic")){
            referenceCode = payment.getClientAdvertisement().getClientAdvertisementCode();
        }
        if (payment.getAbroadOrDomestic().equals("Abroud")){
            referenceCode = payment.getAdAbroud().getBankID();
        }


        try {
            RegisteredCards registeredCards = addRegisteredCards(cardInformationsDto, payment);

        } catch (Exception e) {

            e.printStackTrace();
        }

        String paymentEncryptId = aesUtil.encrypt(String.valueOf(payment.getId()));
        String URLSUCCESS = URL + paymentEncryptId;


        if(ENVIRONMENT.equals("dev")){
            return URLSUCCESS;
        }else{
            return paymentServiceMoka.processPayment(cardInformationsDto, String.valueOf(payment.getTotalPrice()), referenceCode,String.valueOf(payment.getId()), URLSUCCESS);
        }
    }
    public Payment addPaymentTable(CardInformationsDto cardInformationsDto){
        Payment payment = new Payment();
        Double goodPrice = null;
        String startCountryCode = "TR";
        String dueCountryCode =     "TR";
        String advertCurrenty = null;
        String insuranceCurreny = null;
        Double advertBidPrice = 0D;
        Double totalPrice = 0D;
        AdAbroud adAbroud = null;
        ClientAdvertisement clientAdvertisement = null;


        if (cardInformationsDto.getAbroadOrDomestic().equals("Abroud")){
            adAbroud = abroudService.IdFind(Integer.valueOf(cardInformationsDto.getTransportID())).get(0);

            goodPrice = adAbroud.getGoodsPrice();

            startCountryCode = adAbroud.getStartSelectCountryCode();
            dueCountryCode = adAbroud.getDueSelectCountryCode();
            advertCurrenty = adAbroud.getCurrencyUnitId();
            insuranceCurreny = "$";
            advertBidPrice = Double.valueOf(abroudBidService.findbidPaymentSuccessAproved(Integer.valueOf(cardInformationsDto.getTransportID())).get(0).getBid());
            payment.setAbroadOrDomestic("Abroud");
        }
        if(cardInformationsDto.getAbroadOrDomestic().equals("Domastic")){
            clientAdvertisement = clientAdvertisementService.findDataById(Long.valueOf(cardInformationsDto.getTransportID()));

            goodPrice = clientAdvertisement.getGoodsPrice();
            advertCurrenty = clientAdvertisement.getCurrencyUnit().getCurrencySymbol();
            insuranceCurreny = "₺";
            payment.setAbroadOrDomestic("Domastic");
            advertBidPrice = clientAdvertisementBidService.findAllByClientAdvertisementIdRepo(Long.valueOf(cardInformationsDto.getTransportID())).get(0).getBidWithVat();
        }



        // Burada insuranceCurreny kullanmamın nedeni şuan için kolaylık olsun diye
        // Eğer Euro ile teklif almaya başlarsak o zaman teklif para birimine göre yeniden yazılmalı
        if(insuranceCurreny.equals("₺")){
            payment.setRate(1D);
        }
        if(insuranceCurreny.equals("$")){
            Double usd = Double.valueOf(tcmbApiService.getUsd());
            payment.setRate(usd);
        }


        totalPrice = advertBidPrice;
        Insurance insurance = null;
        if(!cardInformationsDto.getInsuranceType().equals("UNINSURED")){
            insurance = insuranceService.insurance(goodPrice, startCountryCode, dueCountryCode,   advertCurrenty, cardInformationsDto.getInsuranceType(), insuranceCurreny);
            if(!insurance.getInsuranceType().equals("UNINSURED")){
                totalPrice = (advertBidPrice * payment.getRate()) + insurance.getInsurancePriceTl();

                insurance = insuranceService.save(insurance);
            }else {
                insurance = null;
            }
        }

        payment.setInsurance(insurance);
        payment.setPaymentStatus("record");
        payment.setAdvertPrice(advertBidPrice);
        payment.setTotalPrice(totalPrice);
        payment.setUser(securityContextUtil.getCurrentClient());
        payment.setAdAbroud(adAbroud);
        payment.setClientAdvertisement(clientAdvertisement);


        payment.setAdvertCurrencySymbol(insuranceCurreny);

        return paymentMokaRepository.save(payment);
    }
    @Transactional
    public RegisteredCards addRegisteredCards(CardInformationsDto cardInformationsDto, Payment payment) throws Exception {
        Long userId = securityContextUtil.getCurrentClient().getId();
        Client user = clientService.findDataById(userId);

        if (cardInformationsDto.isRegistery() && payment.getPaymentStatus().equals("record")) {
            RegisteredCards registeredCards = new RegisteredCards();
            String cardNumber = cardInformationsDto.getCardNumber();


            if (cardNumber.charAt(0) == '4') {
                registeredCards.setCardType("Visa");
            } else if (cardNumber.charAt(0) == '5') {
                registeredCards.setCardType("MasterCard");
            } else if (cardNumber.charAt(0) == '3'){
                registeredCards.setCardType("AmericanExpress");
            }else if (cardNumber.charAt(0) == '6'){
                registeredCards.setCardType("Discover");
            }else{
                registeredCards.setCardType("Diğer");
            }

            // Kart bilgilerinin şifrelendiği bölüm

            String encryptedHolderName = aesUtil.encrypt(cardInformationsDto.getCardHolderFullName());
            registeredCards.setCardHolderFullName(encryptedHolderName);
            String encryptedCardNumber = AESUtil.encrypt(cardInformationsDto.getCardNumber());
            registeredCards.setCardNumber(encryptedCardNumber);
            String encryptedCvcNumber = aesUtil.encrypt(cardInformationsDto.getCvcNumber());
            registeredCards.setCvcNumber(encryptedCvcNumber);


            CardDatesDto cardDatesDto = new CardDatesDto();

            String encryptMonth = aesUtil.encrypt(cardInformationsDto.getExpDates().getExpMonth());
            cardDatesDto.setExpMonth(encryptMonth);
            String encryptYear = aesUtil.encrypt(cardInformationsDto.getExpDates().getExpYear());
            cardDatesDto.setExpYear(encryptYear);

            registeredCards.setCardName(cardInformationsDto.getCardName());
            registeredCards.setExpDates(cardDatesDto);
            registeredCards.setUserId(securityContextUtil.getCurrentClient());
            return registeredCardsRepository.save(registeredCards);
        }
        return null;
    }
    public  String successPaymentDev(Model model, String paymentID){
        Payment payment = paymentMokaRepository.findById(Long.valueOf(paymentID)).get();



        String URL = null;
        if (payment.getAbroadOrDomestic().equals("Domastic")){
            ClientAdvertisement clientAdvertisement = payment.getClientAdvertisement();

            clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.PAYMENT_SUCCESSFUL);

            clientAdvertisementService.saveRepo(clientAdvertisement);
            URL = Frontend+"/client/transport-detail/"+clientAdvertisement.getId();

            if (payment.getInsurance() != null){
                insuranceService.sendMail(String.valueOf(payment.getClientAdvertisement().getId()), payment.getInsurance());
            }
        }
        if (payment.getAbroadOrDomestic().equals("Abroud")){
            abroudService.advertStatusStep(payment.getAdAbroud().getId());

            URL = Frontend+"/client/transport-detail-abroud/"+payment.getAdAbroud().getId();

            if (payment.getInsurance() != null){
                insuranceService.sendMail(String.valueOf(payment.getAdAbroud().getId()), payment.getInsurance());
            }
        }

        payment.setPaymentStatus("success");
        paymentMokaRepository.save(payment);

        model.addAttribute("title", "Ödeme başarılı");
        model.addAttribute("message", "İlan sayfasına yönlendirili yorsunuz");
        model.addAttribute("url", URL);



        return "payment_success";
    }
    public String successPayment(Model model, String paymentID) throws Exception {
        Long paymentIDs = Long.valueOf(aesUtil.decrypt(paymentID));
        Payment payment = paymentMokaRepository.findById(Long.valueOf(paymentIDs)).get();


        String URL = null;
        if (payment.getAbroadOrDomestic().equals("Domastic")){
            ClientAdvertisement clientAdvertisement = payment.getClientAdvertisement();
            clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.PAYMENT_SUCCESSFUL);
            clientAdvertisementService.saveRepo(clientAdvertisement);


            TransportProcess transportProcess = clientAdvertisement.getClientTransportProcess();
            transportProcess.setTransportProcessStatus(TransportProcessStatus.PAYMENT_SUCCESSFUL);
            transportProcessService.saveRepo(transportProcess);


            URL = Frontend+"/client/transport-detail/"+clientAdvertisement.getId();

            if (payment.getInsurance() != null){
                insuranceService.sendMail(String.valueOf(payment.getClientAdvertisement().getId()), payment.getInsurance());
            }
        }
        if (payment.getAbroadOrDomestic().equals("Abroud")){
            AdAbroud adAbroud = payment.getAdAbroud();
            adAbroud.setAdvertisementStatus("RATING");
            abroudService.save(adAbroud);

            URL = Frontend+"/client/transport-detail-abroud/"+payment.getAdAbroud().getId();

            if (payment.getInsurance() != null){
                insuranceService.sendMail(String.valueOf(payment.getAdAbroud().getId()), payment.getInsurance());
            }
        }

        payment.setPaymentStatus("success");
        paymentMokaRepository.save(payment);

        model.addAttribute("title", "Ödeme başarılı");
        model.addAttribute("message", "İlan sayfasına yönlendirili yorsunuz");
        model.addAttribute("url", URL);



        return "payment_success";
    }

    public void afterPay(String advertID, String insuranceType){
        AfterPay afterPay = new AfterPay();

        AdAbroud adAbroud = abroudService.IdFind(Integer.parseInt(advertID)).get(0);
        afterPay.setAdAbroud(adAbroud);
        afterPay.setInsuranceType(insuranceType);

        afterPayRepository.save(afterPay);

        abroudService.advertStatusStep(Integer.parseInt(advertID));

    }
    public String getAfterPay(String advertID){
       return afterPayRepository.findAfterPayByAdAbroud_Id(Integer.parseInt(advertID)).get().getInsuranceType();
    }
    public String failPayment(Model model, String paymentID, String resultCode) throws Exception {
        Long paymentIDs = Long.valueOf(aesUtil.decrypt(paymentID));
        Payment payment = paymentMokaRepository.findById(Long.valueOf(paymentIDs)).get();

        String URL = null;
        if (payment.getAbroadOrDomestic().equals("Domastic")){
            URL = Frontend+"/client/transport-detail/"+payment.getClientAdvertisement().getId();
        }
        if (payment.getAbroadOrDomestic().equals("Abroud")){
            URL = Frontend+"/client/transport-detail-abroud/"+payment.getAdAbroud().getId();
        }

        payment.setPaymentStatus(resultCode);
        paymentMokaRepository.save(payment);

        String failMessage = null;
        switch (resultCode){
            case "001":
                failMessage = "Kart Sahibi Onayı Alınamadı";
                break;
            case "002":
                failMessage = "Limit Yetersiz";
                break;
            case "003":
                failMessage = "Kredi Kartı Numarası Geçerli Formatta Değil";
                break;
            case "005":
                failMessage = "Kart Sahibine Açık Olmayan İşlem";
                break;
            case "006":
                failMessage = "Kartın Son Kullanma Tarihi Hatali";
                break;
            case "007":
                failMessage = "Geçersiz İşlem";
                break;
            case "008":
                failMessage = "Bankaya Bağlanılamadı";
                break;
            case "011":
                failMessage = "Manual Onay İçin Bizi Arayınız";
                break;
            case "012":
                failMessage = "Kart Bilgileri Hatalı - Kart No veya CVV2";
                break;
            case "013":
                failMessage = "Visa MC Dışındaki Kartlar 3D Secure Desteklemiyor";
                break;
            case "015":
                failMessage = "Geçersiz CVV";
                break;
            case "018":
                failMessage = "Çalıntı Kart";
                break;
            case "019":
                failMessage = "Kayıp Kart";
                break;
            case "020":
                failMessage = "Kısıtlı Kart";
                break;
            case "021":
                failMessage = "Zaman Aşımı";
                break;
            case "024":
                failMessage = "3D Onayı Alındı Ancak Para Karttan Çekilemedi";
                break;
            case "025":
                failMessage = "3D Onay Alma Hatası";
                break;
            case "029":
                failMessage = "Kartınız e-ticaret İşlemlerine Kapalıdır";
                break;
            default:
                failMessage = "Bizimle İletişime Geçin";

        }

        model.addAttribute("failMessage", failMessage);
        model.addAttribute("title", "Ödeme başarısız oldu");
        model.addAttribute("message", "İlan sayfasına yönlendirili yorsunuz");
        model.addAttribute("url", URL);

        return "payment_fail";
    }
    public List<CardNameAndNumberDto> getCardDataByUserId() {
        Long userId = securityContextUtil.getCurrentUserId();
        List<RegisteredCards> cards = registeredCardsRepository.findByUserId(userId);
        List<CardNameAndNumberDto> cardDTOs = new ArrayList<>();


        for (RegisteredCards card : cards) {
            try {
                String decryptedCardNumber = AESUtil.decrypt(card.getCardNumber());
                CardNameAndNumberDto cardDTO = new CardNameAndNumberDto(card.getCardName(), decryptedCardNumber);
                String encryptedPaymentId = AESUtil.encrypt(String.valueOf(card.getId()));
                cardDTO.setPaymentId(encryptedPaymentId);
                cardDTO.setCardType(card.getCardType());
                cardDTOs.add(cardDTO);


            } catch (Exception e) {
                throw new RuntimeException("Failed to decrypt card number", e);
            }
        }
        return cardDTOs;
    }
    public String sendSelectedCardToMoka(SelectedCardDto selectedCardDto, HttpServletResponse response) throws Exception {

        String decryptPaymentId;
        RegisteredCards registeredCards;

        try {
            decryptPaymentId = AESUtil.decrypt(selectedCardDto.getPaymentId());

        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt paymentID", e);
        }
        registeredCards = registeredCardsRepository.findById(Long.valueOf(decryptPaymentId)).get();
        CardInformationsDto cardInformationsDto = new CardInformationsDto();
        cardInformationsDto.setAbroadOrDomestic(selectedCardDto.getAbroudorDomestic());
        cardInformationsDto.setTransportID(selectedCardDto.getAdvertId());
        cardInformationsDto.setRegistery(false);
        try {
            String decryptedCardHolder = AESUtil.decrypt(registeredCards.getCardHolderFullName());
            cardInformationsDto.setCardHolderFullName(decryptedCardHolder);

            String decryptedCardNumber = AESUtil.decrypt(registeredCards.getCardNumber());
            cardInformationsDto.setCardNumber(decryptedCardNumber);


            String decryptedCVC = AESUtil.decrypt(registeredCards.getCvcNumber());
            cardInformationsDto.setCvcNumber(decryptedCVC);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt smt", e);
        }
        CardDatesDto cardDatesDto = new CardDatesDto();
        try {
            String decryptedExpMonth = AESUtil.decrypt(registeredCards.getExpDates().getExpMonth());
            String decryptedExpYear = AESUtil.decrypt(registeredCards.getExpDates().getExpYear());
            cardDatesDto.setExpMonth(decryptedExpMonth);
            cardDatesDto.setExpYear(decryptedExpYear);}
        catch (Exception e) {
            throw new RuntimeException("Failed to decrypt cardDatesDTO decrypt", e);
        }
        cardInformationsDto.setExpDates(cardDatesDto);
        cardInformationsDto.setInsuranceType("NARROW");
        return payBridge(cardInformationsDto, response);
    }

}


