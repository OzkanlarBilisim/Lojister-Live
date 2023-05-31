package com.lojister.controller.abroudControler;


import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.util.annotation.ActiveDriver;
import com.lojister.core.util.annotation.DriverBoss;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.entity.User;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.driver.Driver;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.repository.advertisement.ClientAdvertisementBidRepository;
import com.lojister.service.abroudService.AbroudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/abroud")
public class AbroudController {

    @Autowired
    private AbroudService abroudService;
    @Autowired
    private AbroudRepository abroudRepository;
    @Autowired
    private SecurityContextUtil securityContextUtil;


    @PostMapping("/add")
    public String add(@RequestBody AdAbroud student){
        abroudService.saveStudent(student);
        return "Yeni ilan eklendi";
    }


    @GetMapping("/getAll")
    public List <AdAbroud> findByActive(){
        return abroudRepository.findAllActiveUsers();
    }
    @PostMapping("/advertStatusStep/{advertId}")
    public String advertStatusStep(@PathVariable int advertId){
        return  abroudService.advertStatusStep(advertId);
    }



    @GetMapping("/{abroudID}")
    public AdAbroud getOneAbroud(@PathVariable int abroudID){
        return abroudRepository.findById(abroudID).orElse(null);
    }
    @GetMapping("/client/{clientID}")
    public List <AdAbroud> findClientAbroudt(@PathVariable String clientID){
        return abroudRepository.findClientAbroud(clientID);
    }


    @PutMapping("/statusUpdate/{id}")
    public ResponseEntity<AdAbroud> updateEmployee(@PathVariable int id, @RequestBody AdAbroud employeeDetails) {
        AdAbroud updateEmployee = abroudRepository.findById(id).orElseThrow((null));

        updateEmployee.setAdvertisementStatus(employeeDetails.getAdvertisementStatus());

        abroudRepository.save(updateEmployee);

        return ResponseEntity.ok(updateEmployee);
    }
}
