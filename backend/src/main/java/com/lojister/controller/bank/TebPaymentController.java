package com.lojister.controller.bank;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.enums.InsuranceType;
import com.lojister.business.abstracts.PaymentService;
import com.lojister.core.api.ApiPaths;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.service.abroudService.PaymentControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/payment")
@CrossOrigin
@Authenticated
public class TebPaymentController {

    private final PaymentService paymentService;
    @Autowired
    private AbroudBidRepository abroudBidRepository;
    @Autowired
    private AbroudRepository abroudRepository;

    @Autowired
    private PaymentControlService paymentControlService;

    public TebPaymentController(@Qualifier("tebPaymentServiceImpl") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping(value = "/3d")
    @PermitAllCustom
    public String paymentOperation(Model model, @RequestParam String transportCode,@RequestParam(required = false) InsuranceType insuranceType) throws NoSuchAlgorithmException {
        return paymentService.paymentOperation(model, transportCode,insuranceType);
    }
    @GetMapping(value = "/abroud/3d")
    @PermitAllCustom
    public String paymentOperationAbroud(Model model, @RequestParam int transportCode,@RequestParam("insuranceType") String insuranceType) throws NoSuchAlgorithmException {
        return paymentService.paymentOperationAbroud(model, transportCode,insuranceType);
    }

    @PostMapping("/success")
    @PermitAllCustom
    public String successPayment(Model model, @RequestParam("transportCode") String transportCode, @RequestParam("key") String key) {
        paymentControlService.successPaymentDomestic(transportCode, key);
        return paymentService.successPayment(model, transportCode);
    }

    @PostMapping("/fail")
    @PermitAllCustom
    public String failPayment(Model model, @RequestParam String transportCode, HttpServletRequest request) {
        return paymentService.failPayment(model,transportCode);
    }

    @PostMapping("/abroud/success")
    @PermitAllCustom
    public String successPaymentAbroud(Model model, @RequestParam int transportCode) {
        AdAbroud updateAbroudStatus = abroudRepository.IdFind(transportCode).get(0);

        if(updateAbroudStatus.getAdvertisementStatus().equals("APPROVED")){
            abroudBid updateEmployee = abroudBidRepository.findbidAndabraud(transportCode).get(0);
            updateEmployee.setStatus("PAYMENT_SUCCESSFUL");

            abroudBidRepository.save(updateEmployee);
        }else{
            // return "redirect:http://localhost:3000/client/transport-detail-abroud/"+transportCode;
        }

        updateAbroudStatus.setAdvertisementStatus("PAYMENT_SUCCESSFUL");

        abroudRepository.save(updateAbroudStatus);

        model.addAttribute("title", "Ödeme başarılı");
        model.addAttribute("message", "Çarpıya basıp pencereyi kapatın.");

        return "payment_success_abroud";
    }


    @GetMapping("test/success")
    @PermitAllCustom
    public String successPaymenttest(Model model) {
        model.addAttribute("title", "Ödeme başarılı");
        model.addAttribute("message", "İlan sayfasına yönlendiriliyorsunuz.");

        return "payment_success";
    }


    @PostMapping("/abroud/fail")
    @PermitAllCustom
    public String failPaymentAbroud(Model model) {
        model.addAttribute("title", "Ödeme Başarsız");
        model.addAttribute("message", "Çarpıya basıp pencereyi kapatın.");

        return "payment_fail_abroud";
    }

}
