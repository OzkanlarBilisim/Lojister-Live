package com.lojister.service.teb;

import com.est.jpay;
import com.lojister.business.abstracts.*;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.core.util.annotation.SaveInsuredTransportProcessAfterSuccessPayment;
import com.lojister.core.util.annotation.SendMailAfterPaymentToInsuranceCompany;
import com.lojister.model.dto.HalkbankPayment3dDto;
import com.lojister.model.dto.TebPayment3dDto;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.InsuranceType;
import com.lojister.model.enums.Language;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.entity.TransportProcess;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.service.abroudService.AbroudBidService;
import com.lojister.service.abroudService.AbroudService;
import com.lojister.service.abroudService.PaymentControlService;
import com.lojister.service.api.TcmbApiService;
import com.lojister.service.jpay.JpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import sun.misc.BASE64Encoder;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class TebPaymentServiceImpl implements PaymentService {

    private final ClientTransportProcessService clientTransportProcessService;
    private final AbroudBidService abroudBidService;
    private final AbroudService abroudService;
    private final UserService userService;
    private final  TransportProcessService transportProcessService;

    private final JpayService jpayService;

    private final ClientAdvertisementService clientAdvertisementService;
    @Autowired
    private TcmbApiService tcmbApiService;

    private static final String CLIENT_ID = "400237496";
    private static final String STORE_KEY = "CEw3P4uH";
    private static final String NAME = "ozkanlaradmin";
    private static final String PASSWORD = "OZKN8970";
    private static final String CURRENCY = "949";
    private static final String TYPE = "Auth";
    private static final String LANG = "tr";
    private static final String REFRESH_TIME = "3";
    private static final String F_ULKE_KOD = "tr";
    private static final String T_ULKE_KOD = "tr";
    private static final String STORE_TYPE = "3d_pay_hosting";

    @Value("${lojister.payment.teb.okUrl}")
    private String OK_URL;  //İşlem başarılıysa dönülecek işyeri sayfası  (3D işleminin ve ödeme işleminin sonucu)
    @Value("${lojister.payment.teb.okUrlAbroud}")
    private String OK_URL_ABROUD;
    @Value("${lojister.payment.teb.failUrl}")
    private String FAIL_URL;
    @Value("${lojister.payment.teb.failUrlAbroud}")
    private String FAIL_URL_ABROUD;

    @Autowired
    PaymentControlService paymentControlService;
    private static final String HOST = "sanalpos.teb.com.tr";
    private static final int PORT = 443;
    private static final String PATH = "/fim/api";


    //TODO TRANSPORT CODE KONTROL ETTIR. MIN KARAKTER.
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

        clientTransportProcess = clientTransportProcessService.calculateTheFeeByInsuranceType(clientTransportProcess);

        OK_URL = OK_URL + transportCode + "&key="+paymetKey;

        TebPayment3dDto tebPayment3DDto = new TebPayment3dDto();
        tebPayment3DDto.setClientid(CLIENT_ID);
        //tebPayment3DDto.setAmount(clientTransportProcess.getPrice().toString());
        tebPayment3DDto.setAmount(clientTransportProcess.getPayAmount());
        tebPayment3DDto.setOid(clientTransportProcess.getTransportCode());
        tebPayment3DDto.setOkUrl(OK_URL);
        tebPayment3DDto.setFailUrl(FAIL_URL + transportCode);
        tebPayment3DDto.setIslemtipi(TYPE);
        tebPayment3DDto.setTaksit("");
        tebPayment3DDto.setCurrency(CURRENCY);
        tebPayment3DDto.setRnd(new Date().toString());

        String hashString = CLIENT_ID + clientTransportProcess.getTransportCode() + clientTransportProcess.getPayAmount() +
                OK_URL + FAIL_URL + clientTransportProcess.getTransportCode() + TYPE + tebPayment3DDto.getRnd() + STORE_KEY;
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        tebPayment3DDto.setHash(new BASE64Encoder().encode(sha1.digest(hashString.getBytes())));
        tebPayment3DDto.setTel(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getPhone());
        tebPayment3DDto.setFaturaFirma(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getFirstName() + " " +
                clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getLastName());
        tebPayment3DDto.setLang(LANG);
        tebPayment3DDto.setStoretype(STORE_TYPE);
        tebPayment3DDto.setRefreshtime(REFRESH_TIME);
        tebPayment3DDto.setFulkekod(F_ULKE_KOD);
        tebPayment3DDto.setTulkekod(T_ULKE_KOD);
        String title = Translator.toLocale("lojister.bank.title");
        String message=Translator.toLocale("lojister.bank.message");
        model.addAttribute("title",title);
        model.addAttribute("message",message);
        model.addAttribute("m3d", tebPayment3DDto);

        return "3d_pay";

    }

    @Override
    public String paymentOperationAbroud(Model model, int transportCode, String insuranceType) throws NoSuchAlgorithmException {


        com.lojister.model.abroudModel.abroudBid   abroudBid2  = abroudBidService.findbidAndabraud(transportCode).get(0);
        com.lojister.model.abroudModel.AdAbroud    adAbroud2   = abroudService.IdFind(transportCode).get(0);
        com.lojister.model.entity.User             user2       = userService.findDataById(Long.valueOf(adAbroud2.getClient_id()));

        String money = null;
        switch (insuranceType){
            case "UNINSURED":
                money = abroudBid2.getBid();
                break;
            case "NARROW":
                if (abroudBid2.getAdAbroud().getGoodsPrice() == null){
                    money = abroudBid2.getBid();
                }else {
                    double goodsPrice = abroudBid2.getAdAbroud().getGoodsPrice();
                    if(goodsPrice > 1.600){
                        money = String.valueOf((goodsPrice * 0.001155) + Double.parseDouble(abroudBid2.getBid()));
                    }else {
                        money = String.valueOf(2 + Integer.parseInt(abroudBid2.getBid()));
                    }
                }
                break;
            default:
                money = abroudBid2.getBid();
                break;
        }

        abroudBidService.findbidAndabraud(transportCode);





        TebPayment3dDto tebPayment3DDto = new TebPayment3dDto();
        tebPayment3DDto.setClientid(CLIENT_ID);
        tebPayment3DDto.setAmount(money);
        tebPayment3DDto.setOid(adAbroud2.getBankID() + transportCode);
        tebPayment3DDto.setOkUrl(OK_URL_ABROUD + transportCode);
        tebPayment3DDto.setFailUrl(FAIL_URL_ABROUD + transportCode);
        tebPayment3DDto.setIslemtipi(TYPE);
        tebPayment3DDto.setTaksit("");
        tebPayment3DDto.setCurrency("949");
        tebPayment3DDto.setRnd(new Date().toString());

        String hashString = CLIENT_ID + adAbroud2.getBankID() + transportCode + money +
                OK_URL_ABROUD + transportCode + FAIL_URL_ABROUD + transportCode + TYPE + tebPayment3DDto.getRnd() + STORE_KEY;
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        tebPayment3DDto.setHash(new BASE64Encoder().encode(sha1.digest(hashString.getBytes())));
        tebPayment3DDto.setTel(user2.getPhone());
        tebPayment3DDto.setFaturaFirma(user2.getFirstName()+" "+user2.getLastName());
        tebPayment3DDto.setLang(LANG);
        tebPayment3DDto.setStoretype(STORE_TYPE);
        tebPayment3DDto.setRefreshtime(REFRESH_TIME);
        tebPayment3DDto.setFulkekod(F_ULKE_KOD);
        tebPayment3DDto.setTulkekod(T_ULKE_KOD);

        model.addAttribute("m3d", tebPayment3DDto);
        String title = Translator.toLocale("lojister.bank.title", Language.toLocale(Language.TURKISH));
        String message = Translator.toLocale("lojister.bank.message", Language.toLocale(Language.TURKISH));
        model.addAttribute("title", title);
        model.addAttribute("message", message);

        return "3d_pay";
    }

    public String paymentSuccess(Model model,TransportProcess transportProcess) {
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
    public String successPayment(Model model,String transportCode) {

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
    public String successPaymentAbroud(Model model,int transportCode) {

        TransportProcess transportProcess = transportProcessService.findDataByTransportCode(String.valueOf(transportCode));

        jpay myjpay = new jpay();
        myjpay.setName(NAME);
        myjpay.setPassword(PASSWORD);
        myjpay.setClientId(CLIENT_ID);
        myjpay.setOrderId(String.valueOf(transportCode));
        myjpay.setExtra("ORDERSTATUS", "QUERY");
        Boolean isPayment = jpayService.checkPayment(myjpay, HOST, PORT, PATH);

       // Boolean isPayment = true;

        if (isPayment) {

            return paymentSuccess(model,transportProcess);

        } else {
            return "payment_fail";
        }

    }

    //TODO CANLIYA SAKIN ATMA !!!!
    @OnlyAdmin
    public Boolean successPaymentTest(String transportCode) {

        TransportProcess transportProcess = transportProcessService.findDataByTransportCode(transportCode);

        transportProcess.setAdmissionDate(LocalDate.now());
        transportProcess.setAdmissionTime(LocalTime.now());
        transportProcess.setDeliveryDate(LocalDate.now());
        transportProcess.setDeliveryTime(LocalTime.now());

        transportProcess.setTransportProcessStatus(TransportProcessStatus.CARGO_WAS_DELIVERED);
        transportProcessService.saveRepo(transportProcess);
        return Boolean.TRUE;

    }

    //TODO BURADAKİ DAHA ÖNCEDEN ÖDENMİŞTİR MESAJINI ALABİLİRSİN.
    @Override
    public String failPayment(Model model,String transportCode) {

        transportProcessService.findDataByTransportCode(transportCode);

        return "payment_fail";
    }


}

