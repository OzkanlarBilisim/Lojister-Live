package com.lojister.controller.abroudControler;

import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.Receipt;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.dto.abroudDto.ReceitDto;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.service.abroudService.AbroudBidService;
import com.lojister.service.abroudService.AbroudService;
import com.lojister.service.abroudService.ReceiptService;
import com.lojister.service.api.TcmbApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    @Autowired
    private AbroudService abroudService;
    @Autowired
    private AbroudBidRepository abroudBidRepository;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private TcmbApiService tcmbApiService;

    @PostMapping("/add")
    public String addReceiptDocument(@RequestParam("file") MultipartFile file, @RequestParam("advertID") int advertID) throws IOException {
        String[] permittedFile = {"application/pdf"};
        AdAbroud adAbroad = abroudService.IdFind(advertID).get(0);
        abroudBid abroudBid = abroudBidRepository.findbidPaymentSuccessAproved(adAbroad.getId()).get(0);
        Double usd = Double.valueOf(tcmbApiService.getUsd());
        if (Arrays.asList(permittedFile).contains(file.getContentType())) {
            Receipt uploadedReceipt = new Receipt();
            uploadedReceipt.setReceipt(file.getBytes());
            uploadedReceipt.setAdAbroud(adAbroad);
            uploadedReceipt.setPrice(Double.valueOf(abroudBid.getBid()) * usd);
            receiptService.save(uploadedReceipt);
            return "Dekont kaydedildi";
        }

        return "Dekont kaydedilemedi";
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
        return receiptService.getFile(id);
    }

    @PostMapping("/approved/{id}/deleteOrAproved/{deleteOrAproved}")
    public void approved(@PathVariable Long id, @PathVariable Boolean deleteOrAproved){
        receiptService.approved(id, deleteOrAproved);
    }
    @GetMapping("/getAll/{isReceipt}")
    public List<ReceitDto> findall(@PathVariable Boolean isReceipt){
        return receiptService.findall(isReceipt);
    }
}
