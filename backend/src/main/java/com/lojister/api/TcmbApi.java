package com.lojister.api;

import com.lojister.service.api.TcmbApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tcmb")
public class TcmbApi {
    @Autowired
    private TcmbApiService tcmbApiService;

    @GetMapping("/dolar")
    public String getUsd(){
        return tcmbApiService.getUsd();
    }

    @GetMapping("/euro")
    public String getEuro(){
        return tcmbApiService.getEuro();
    }
}
