package com.lojister.service.halkbank;

import com.est.jpay;
import com.google.api.client.http.HttpHeaders;
import com.lojister.business.abstracts.*;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.SaveInsuredTransportProcessAfterSuccessPayment;
import com.lojister.core.util.annotation.SendMailAfterPaymentToInsuranceCompany;
import com.lojister.model.abroudModel.PaymentControl;
import com.lojister.model.dto.HalkbankPayment3dDto;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.InsuranceType;
import com.lojister.model.enums.Language;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.entity.TransportProcess;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.other.Other;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.service.abroudService.AbroudBidService;
import com.lojister.service.abroudService.AbroudService;
import com.lojister.service.abroudService.PaymentControlService;
import com.lojister.service.api.TcmbApiService;
import com.lojister.service.jpay.JpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import sun.misc.BASE64Encoder;

import java.math.BigDecimal;
import java.util.Base64;

import javax.transaction.Transactional;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class HalkbankPaymentServiceImpl implements PaymentService {

    private final TransportProcessService transportProcessService;
    private final AbroudBidService abroudBidService;
    private final AbroudService abroudService;
    private final UserService userService;
    private final ClientTransportProcessService clientTransportProcessService;
    private final JpayService jpayService;

    private final ClientAdvertisementService clientAdvertisementService;
    private final MailNotificationService mailNotificationService;
    private final PushNotificationService pushNotificationService;

    private static final String CLIENT_ID = "500392116";
    private static final String STORE_KEY = "JYt3x5Ea";
    private static final String NAME = "ozkanlaradmin";
    private static final String PASSWORD = "HGYT9090";
    private static final String CURRENCY = "949";
    private static final String TYPE = "Auth";
    private static final String LANG = "tr";
    private static final String REFRESH_TIME = "3";
    private static final String F_ULKE_KOD = "tr";
    private static final String T_ULKE_KOD = "tr";
    private static final String STORE_TYPE = "3d_pay_hosting";
    private static final String MODE = "T";

    @Value("${lojister.payment.halkbank.okUrl}")
    private String OK_URL;  //İşlem başarılıysa dönülecek işyeri sayfası  (3D işleminin ve ödeme işleminin sonucu)
    @Value("${lojister.payment.halkbank.okUrlAbroud}")
    private String OK_URL_ABROUD;  //İşlem başarılıysa dönülecek işyeri sayfası  (3D işleminin ve ödeme işleminin sonucu)

    @Value("${lojister.payment.halkbank.failUrl}")
    private String FAIL_URL;
    @Value("${lojister.payment.halkbank.failUrlAbroud}")
    private String FAIL_URL_ABROUD;

    private static final String HOST = "sanalpos.halkbank.com.tr";
    private static final int PORT = 443;
    private static final String PATH = "/fim/api";
    private final AbroudBidRepository abroudBidRepository;
    private final PaymentControlService paymentControlService;
    private final Other other;

    @Autowired
    private TcmbApiService tcmbApiService;


    //TODO SADECE BELLİ AŞAMALARDA ÖDEME YAPILABİLMESİ İÇİN BUSINESS EKLENECEK.
    @Override
    public String paymentOperation(Model model, String transportCode, InsuranceType insuranceType) throws NoSuchAlgorithmException {
        String paymetKey = paymentControlService.addNewPaymentDomestic(transportCode);

        transportProcessService.transportCodeStartsWithCheck(transportCode);

        ClientTransportProcess clientTransportProcess = clientTransportProcessService.findDataByTransportCode(transportCode);

        if (insuranceType == null) {
            clientTransportProcess.setInsuranceType(InsuranceType.UNINSURED);
        } else {
            clientTransportProcess.setInsuranceType(insuranceType);
        }
        clientTransportProcess.setEuro(Double.valueOf(tcmbApiService.getEuro()));
        clientTransportProcess.setDolar(Double.valueOf(tcmbApiService.getUsd()));

        OK_URL = OK_URL + transportCode + "&key="+paymetKey;

        clientTransportProcess = clientTransportProcessService.calculateTheFeeByInsuranceType(clientTransportProcess);

        HalkbankPayment3dDto halkbankPayment3dDto = new HalkbankPayment3dDto();
        halkbankPayment3dDto.setClientid(CLIENT_ID);
        halkbankPayment3dDto.setAmount(clientTransportProcess.getPayAmount());
        halkbankPayment3dDto.setOid(clientTransportProcess.getTransportCode());
        halkbankPayment3dDto.setOkUrl(OK_URL);
        halkbankPayment3dDto.setFailUrl(FAIL_URL + transportCode);
        halkbankPayment3dDto.setIslemtipi(TYPE);
        halkbankPayment3dDto.setTaksit("");
        halkbankPayment3dDto.setCurrency(CURRENCY);
        halkbankPayment3dDto.setRnd(new Date().toString());

        String hashString = CLIENT_ID + clientTransportProcess.getTransportCode() + clientTransportProcess.getPayAmount() +
                OK_URL + FAIL_URL + clientTransportProcess.getTransportCode() + TYPE + halkbankPayment3dDto.getRnd() + STORE_KEY;
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        halkbankPayment3dDto.setHash(new BASE64Encoder().encode(sha1.digest(hashString.getBytes())));
        halkbankPayment3dDto.setTel(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getPhone());
        halkbankPayment3dDto.setFaturaFirma(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getFirstName() + " " +
                clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getLastName());
        halkbankPayment3dDto.setLang(LANG);
        halkbankPayment3dDto.setStoretype(STORE_TYPE);
        halkbankPayment3dDto.setRefreshtime(REFRESH_TIME);
        halkbankPayment3dDto.setFulkekod(F_ULKE_KOD);
        halkbankPayment3dDto.setTulkekod(T_ULKE_KOD);

        model.addAttribute("halkbank3d", halkbankPayment3dDto);

        model.addAttribute("title", "Ödeme başarılı");
        model.addAttribute("message", "Çarpıya basıp pencereyi kapatın.");
        return "halkbank3d_pay";
    }
    @Override
    public String paymentOperationAbroud(Model model, int transportCode, String insuranceType) throws NoSuchAlgorithmException {

        // Ödeme Sırasında heklenmemek için ekstra güvenlik önlemi
        String paymetKey = paymentControlService.addNewPayment(transportCode);



        com.lojister.model.abroudModel.abroudBid   abroudBid2  = abroudBidService.findbidAndabraud(transportCode).get(0);
        com.lojister.model.abroudModel.AdAbroud    adAbroud2   = abroudService.IdFind(transportCode).get(0);
        com.lojister.model.entity.User             user2       =  userService.findDataById(Long.valueOf(adAbroud2.getClient_id()));




        String money = null;
        String insuranceCost = "0";
        switch (insuranceType){
            case "UNINSURED":
                money = abroudBid2.getBid();
                break;

            case "NARROW":
                if (abroudBid2.getAdAbroud().getGoodsPrice() == null){
                    money = abroudBid2.getBid();
                }else {
                    double goodsPrice = abroudBid2.getAdAbroud().getGoodsPrice();
                    if(goodsPrice >= 1600){
                        money = String.valueOf(Math.ceil(goodsPrice * 0.001155) + Double.parseDouble(abroudBid2.getBid()));
                        insuranceCost = String.valueOf(goodsPrice * 0.001155);
                    }else {
                        money = String.valueOf(2.0 + Double.parseDouble(abroudBid2.getBid()));
                        insuranceCost = "2";
                    }
                }
                break;
            default:
                money = abroudBid2.getBid();
                break;
        }


        OK_URL_ABROUD = OK_URL_ABROUD + transportCode + "&InsuranceCost=" + insuranceCost+"&key="+paymetKey;


        HalkbankPayment3dDto halkbankPayment3dDto = new HalkbankPayment3dDto();
        halkbankPayment3dDto.setClientid(CLIENT_ID);
        // halkbankPayment3dDto.setAmount("1");
        halkbankPayment3dDto.setAmount(money);
        halkbankPayment3dDto.setOid(adAbroud2.getBankID() + transportCode);
        halkbankPayment3dDto.setOkUrl(OK_URL_ABROUD);
        halkbankPayment3dDto.setFailUrl(FAIL_URL_ABROUD + transportCode);
        halkbankPayment3dDto.setIslemtipi(TYPE);
        halkbankPayment3dDto.setTaksit("");
        // Dolar ile ödeme almak için
        halkbankPayment3dDto.setCurrency("840");
        // tl için
        //halkbankPayment3dDto.setCurrency("949");
        halkbankPayment3dDto.setRnd(new Date().toString());

        String hashString = CLIENT_ID + adAbroud2.getBankID()+ transportCode + money +
                OK_URL_ABROUD  + FAIL_URL_ABROUD + transportCode + TYPE + halkbankPayment3dDto.getRnd() + STORE_KEY;


        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        halkbankPayment3dDto.setHash(new BASE64Encoder().encode(sha1.digest(hashString.getBytes())));
        halkbankPayment3dDto.setTel(user2.getPhone());
        halkbankPayment3dDto.setFaturaFirma(user2.getFirstName()+" "+user2.getLastName());
        halkbankPayment3dDto.setLang(LANG);
        halkbankPayment3dDto.setStoretype(STORE_TYPE);
        halkbankPayment3dDto.setRefreshtime(REFRESH_TIME);
        halkbankPayment3dDto.setFulkekod(F_ULKE_KOD);
        halkbankPayment3dDto.setTulkekod(T_ULKE_KOD);


        model.addAttribute("halkbank3d", halkbankPayment3dDto);
        String title = Translator.toLocale("lojister.bank.title", Language.toLocale(Language.TURKISH));
        String message = Translator.toLocale("lojister.bank.message", Language.toLocale(Language.TURKISH));
        model.addAttribute("title", title);
        model.addAttribute("message", message);
        return "halkbank3d_pay";
    }




    public String paymentSuccessAbroud(Model model, TransportProcess transportProcess) {
        String title = Translator.toLocale("Ödemeniz onaylandı", Language.toLocale(Language.TURKISH));
        String message = Translator.toLocale("İlan sayfasına yönlendiriliyorsunuz", Language.toLocale(Language.TURKISH));
        model.addAttribute(title, "title");
        model.addAttribute(message, "message");

        return "payment_success";
    }

    public String paymentSuccess(Model model, TransportProcess transportProcess) {
        transportProcess.setTransportProcessStatus(TransportProcessStatus.PAYMENT_SUCCESSFUL);
        transportProcessService.saveRepo(transportProcess);
        if (transportProcess instanceof ClientTransportProcess) {
            ClientTransportProcess clientTransportProcess = clientTransportProcessService.findDataById(transportProcess.getId());
            Long clientAdvertisementId = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getId();
            clientAdvertisementService.updateClientAdvertisementProcessStatus(clientAdvertisementId, AdvertisementProcessStatus.PAYMENT_SUCCESSFUL);
        }

        model.addAttribute("title", "Ödeme başarılı");
        model.addAttribute("message", "Çarpıya basıp pencereyi kapatın.");
        return "payment_success";
    }

    @Override
    @SendMailAfterPaymentToInsuranceCompany
    @SaveInsuredTransportProcessAfterSuccessPayment
    public String successPayment(Model model, String transportCode) {

        TransportProcess transportProcess = transportProcessService.findDataByTransportCode(transportCode);

        jpay myjpay = new jpay();
        myjpay.setName(NAME);
        myjpay.setPassword(PASSWORD);
        myjpay.setClientId(CLIENT_ID);
        myjpay.setOrderId(transportCode);
        myjpay.setExtra("ORDERSTATUS", "QUERY");
        Boolean isPayment = jpayService.checkPayment(myjpay, HOST, PORT, PATH);

        // Boolean isPayment = true;

        if (isPayment) {

            return paymentSuccess(model,transportProcess);

        } else {
            return "payment_fail";
        }
    }

    @Override
    @SendMailAfterPaymentToInsuranceCompany
    @SaveInsuredTransportProcessAfterSuccessPayment
    public String successPaymentAbroud(Model model, int transportCode) {

        TransportProcess transportProcess = transportProcessService.findDataByTransportCode(String.valueOf(transportCode));
        //TODO ACIKLAMA SATIRINDAN CIKAR SONRA

        jpay myjpay = new jpay();
        myjpay.setName(NAME);
        myjpay.setPassword(PASSWORD);
        myjpay.setClientId(CLIENT_ID);
        myjpay.setOrderId(String.valueOf(transportCode));
        myjpay.setExtra("ORDERSTATUS", "QUERY");

        Boolean isPayment = jpayService.checkPayment(myjpay, HOST, PORT, PATH);
        //  Boolean isPayment = true;

        if (isPayment) {

            return paymentSuccessAbroud(model, transportProcess);

        } else {
            String title = Translator.toLocale("lojister.paymentFail.title",Language.toLocale(((ClientTransportProcess)transportProcess).getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getLanguage()));
            String message = Translator.toLocale("lojister.paymentFail.message",Language.toLocale(((ClientTransportProcess)transportProcess).getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getLanguage()));
            model.addAttribute(title, "title");
            model.addAttribute(message, "message");
            return "payment_fail";
        }
    }

    @Override
    public String failPayment(Model model, String transportCode) {

        transportProcessService.findDataByTransportCode(transportCode);
        return "payment_fail";
}


}