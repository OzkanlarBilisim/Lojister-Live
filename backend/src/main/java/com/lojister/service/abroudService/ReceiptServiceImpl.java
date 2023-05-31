package com.lojister.service.abroudService;

import com.lojister.business.abstracts.MailNotificationService;
import com.lojister.business.abstracts.UserService;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.*;
import com.lojister.model.dto.abroudDto.ReceitDto;
import com.lojister.model.entity.User;
import com.lojister.model.enums.Role;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.repository.abroudRepository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptServiceImpl implements ReceiptService{
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private AbroudService abroudService;
    @Autowired
    private SecurityContextUtil securityContextUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private AbroudBidRepository abroudBidService;
    @Autowired
    private AbroudRepository abroudRepository;
    @Autowired
    private MailNotificationService mailNotificationService;





    public Receipt save(Receipt uploadedReceipt){
        if(uploadedReceipt.getAdAbroud().getAdvertisementStatus().equals("TEMPORARY_METHOD")){
            abroudService.advertStatusStep(uploadedReceipt.getAdAbroud().getId());
            uploadedReceipt.setStatus(false);
            return receiptRepository.save(uploadedReceipt);
        }else{
            throw new RuntimeException("Geçersiz status");
        }
    }

    public Receipt findByID(Long id){
        if(!securityContextUtil.getCurrentUserRole().equals(Role.ROLE_ADMIN)){
            throw new RuntimeException("Bu işlemi yapmak için yetkiniz yok");
        }
        return receiptRepository.findById(id).get();
    }
    public void approved(Long id, Boolean deleteOrAproved){
        if(!securityContextUtil.getCurrentUserRole().equals(Role.ROLE_ADMIN)){
            throw new RuntimeException("Bu işlemi yapmak için yetkiniz yok");
        }
        Receipt receipt = receiptRepository.findById(id).get();
        AdAbroud adAbroud = receipt.getAdAbroud();
        if(deleteOrAproved){
            receipt.setStatus(true);
            receiptRepository.save(receipt);
            adAbroud.setAdvertisementStatus("RATING");
            mailNotificationService.sendStatusChangeAbroud("RATING", adAbroud.getId());
        }else {
            receiptRepository.delete(receipt);
            adAbroud.setAdvertisementStatus("TEMPORARY_METHOD");
            mailNotificationService.sendStatusChangeAbroud("TEMPORARY_METHOD", adAbroud.getId());
        }
        abroudRepository.save(adAbroud);
    }

    public ResponseEntity<byte[]> getFile(Long id) {
        Receipt file = receiptRepository.findById(id).get();


        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(file.getReceipt(), headers, HttpStatus.OK);
    }

    public List<ReceitDto> findall(Boolean isReceipt){
        if(!securityContextUtil.getCurrentUserRole().equals(Role.ROLE_ADMIN)){
            throw new RuntimeException("Senin bu verileri görüntüleme yentkin bulunmamakta");
        }
        if (isReceipt){
            List<Receipt> receipts = receiptRepository.findAllFalce();
            List<ReceitDto> receitDtos = new ArrayList<>();

            for (Receipt receipt :receipts){
                ReceitDto receitDto = new ReceitDto();
                User user = userService.findDataById(Long.valueOf(receipt.getAdAbroud().getClient_id()));
                abroudBid abroudBid1 = abroudBidService.findbidPaymentSuccessAproved(receipt.getAdAbroud().getId()).get(0);

                receitDto.setId(receipt.getId());
                receitDto.setDate(receipt.getAdAbroud().getDateNow());
                receitDto.setMail(user.getEmail());
                receitDto.setTel(user.getPhone());
                receitDto.setCompanyName(user.getFirstName()+" "+user.getLastName());
                receitDto.setPrice(String.valueOf(receipt.getPrice()));
                receitDto.setAdvertID(receipt.getAdAbroud().getId());

                receitDtos.add(receitDto);

            }
            return receitDtos;
        }else {
            List<AdAbroud> adAbrouds = abroudService.getTEMPORARY_METHOD();

            List<ReceitDto> receitDtos = new ArrayList<>();

            for (AdAbroud adAbroud :adAbrouds){
                ReceitDto receitDto = new ReceitDto();
                User user = userService.findDataById(Long.valueOf(adAbroud.getClient_id()));
                abroudBid abroudBid1 = abroudBidService.findbidPaymentSuccessAproved(adAbroud.getId()).get(0);

                receitDto.setDate(adAbroud.getDateNow());
                receitDto.setMail(user.getEmail());
                receitDto.setTel(user.getPhone());
                receitDto.setCompanyName(user.getFirstName()+" "+user.getLastName());
                receitDto.setPrice(abroudBid1.getBid());
                receitDto.setAdvertID(adAbroud.getId());

                receitDtos.add(receitDto);

            }
            return receitDtos;
        }

    }
}
