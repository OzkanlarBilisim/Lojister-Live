package com.lojister.service.abroudService;

import com.lojister.business.abstracts.MailNotificationService;
import com.lojister.business.abstracts.UserService;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.entity.User;
import com.lojister.model.entity.payment.AfterPay;
import com.lojister.model.enums.Role;
import com.lojister.other.Other;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.repository.payment.AfterPayRepository;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class AbroudServiceImpl implements AbroudService {

    @Autowired
    private AbroudRepository abroudRepository;
    @Autowired
    private Other other;
    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private UserService userService;

    @Value("${lojister.site.url}")
    private String FRONTEND_BASE_URL;

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Autowired
    private PaymentControlService paymentControlService;
    @Autowired
    private AbroudBidRepository abroudBidRepository;
    @Autowired
    private AfterPayRepository afterPayRepository;


    public Boolean authorityStatus(String status, int advertID){
        User userInfo = userService.findDataById(securityContextUtil.getCurrentUserId());
        String[] driverAuthority = new String[] {"TRANSPORT", "PAYMENT_SUCCESSFUL", "SHIPSMENTINFO"};
        String[] clientAuthority = new String[] {"WAITING","APPROVED", "RATING", "COMPLETED", "TEMPORARY_METHOD", "TEMPORARY_METHOD2", "WAITING_FOR_TRANSPORT"};


        Boolean authority = false;
        if(userInfo.getRole().equals(Role.ROLE_DRIVER)){
            for (String name : driverAuthority) {
                if (name.equals(status)) {
                    abroudBid abroudBid1 = abroudBidRepository.findbidPaymentSuccessAproved(advertID).get(0);
                    if(abroudBid1.getUserDriver().getId().equals(securityContextUtil.getCurrentUserId())){
                        authority = true;
                    }
                    break;
                }
            }
        }
        if(userInfo.getRole().equals(Role.ROLE_CLIENT)){
            for (String name : clientAuthority) {
                if (name.equals(status)) {
                    AdAbroud abroud = abroudRepository.IdFind(advertID).get(0);
                    if(abroud.getClient_id().equals(String.valueOf(securityContextUtil.getCurrentUserId()))){
                        authority = true;
                    }
                    break;
                }
            }
        }

        return authority;
    }

    public AdAbroud save(AdAbroud adAbroud){
        return abroudRepository.save(adAbroud);
    }
    @Override
    public AdAbroud saveStudent(AdAbroud student) {

        AdAbroud save = null;
        if(securityContextUtil.getCurrentUserRole().equals(Role.ROLE_CLIENT)){
            student.setBankID(other.randomLetter(5)+"=");
            student.setAdvertisementStatus("WAITING");
            save = abroudRepository.save(student);

            User clientINFO = userService.findDataById(Long.valueOf(student.getClient_id()));
            String content = " <b> Sistemde yeni bir ilan verilmiştir. </b>  <br><br>"
                    + "<b><ins> İlanı Açanın Adı Soyadı : </ins> </b>    " + clientINFO.getFirstName()+", "+clientINFO.getLastName() +"<br><br>"
                    + "<b><ins> Başlangıç Adresi: </ins> </b>    " + student.getStartFullAddress() + " <br><br>"
                    + "<b><ins> Varış Adresi: </ins> </b>    " + student.getDueFullAddress() + " <br><br>"
                    + "<b><a href='"+FRONTEND_BASE_URL+"/adsAbroadId/"+save.getId()+"'> İlana gitmek için tıklayın: </a> </b>    " + student.getDueFullAddress() + " <br><br>"
                    + "<b <b> Lojister Yeni İlan Bildirimi. </b>";

            mailNotificationService.send("info@lojister.com", "Yeni yurt dışı ilanı oluşturuldu", content);
            mailNotificationService.sendDriverAbroud("Yeni yurt dışı ilanı oluşturuldu", content);
        }
        return save;
    }
    @Override
    public String advertStatusStep(int advetID){
        AdAbroud advert = abroudRepository.IdFind(advetID).get(0);
        Boolean authority = false;

        if(advert.getAdvertisementStatus().equals("APPROVED")){
            authority = true;
        }else {
            authority = authorityStatus(advert.getAdvertisementStatus(), advetID);
        }

            if(authority){
                String[] advertStatusList = new String[] {"WAITING","APPROVED","PAYMENT_SUCCESSFUL", "SHIPSMENTINFO", "WAITING_FOR_TRANSPORT", "TRANSPORT", "TEMPORARY_METHOD", "TEMPORARY_METHOD2", "RATING", "COMPLETED"};
                int index = Arrays.asList(advertStatusList).indexOf(advert.getAdvertisementStatus());
                index++;
                if(!advert.getAdvertisementStatus().equals("COMPLETED")){
                    if(advert.getAdvertisementStatus().equals("TRANSPORT")){
                        AfterPay afterPay = null;
                        try {
                            afterPay = afterPayRepository.findAfterPayByAdAbroud_Id(advetID).get();
                        }catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        if(afterPay == null){
                            advert.setAdvertisementStatus("RATING");
                        }else {
                            advert.setAdvertisementStatus(advertStatusList[index]);
                        }

                        AdAbroud save =  abroudRepository.save(advert);
                        if (!save.getAdvertisementStatus().equals("COMPLETED") && !save.getAdvertisementStatus().equals("TEMPORARY_METHOD") && !save.getAdvertisementStatus().equals("TEMPORARY_METHOD2")){
                            mailNotificationService.sendStatusChangeAbroud(save.getAdvertisementStatus(), save.getId());
                        }
                    }else {
                        advert.setAdvertisementStatus(advertStatusList[index]);

                        AdAbroud save =  abroudRepository.save(advert);
                        if (!save.getAdvertisementStatus().equals("COMPLETED") && !save.getAdvertisementStatus().equals("TEMPORARY_METHOD") && !save.getAdvertisementStatus().equals("TEMPORARY_METHOD2")){
                            mailNotificationService.sendStatusChangeAbroud(save.getAdvertisementStatus(), save.getId());
                        }
                    }
                }
            }

        return "Güncelleme Tamam";
    }
    @Override
    public List<AdAbroud> IdFind(int id) {
        return abroudRepository.IdFind(id);
    }
    @Override
    public List<AdAbroud> getAllStudents() {
        return abroudRepository.findAll();
    }

    @Override
    public List<AdAbroud> getTEMPORARY_METHOD() {
        return abroudRepository.getTEMPORARY_METHOD();
    }
}
