package com.lojister.controller.abroudControler;

import com.lojister.model.abroudModel.ShipsmentInfo;
import com.lojister.model.dto.abroudDto.ShipsmentInfoDto;
import com.lojister.service.abroudService.ShipsmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipsmentInfo")
public class ShipsmentInfoController {
    @Autowired
    private ShipsmentInfoService shipsmentInfoService;

    @PostMapping("/add")
    public String add(@RequestBody ShipsmentInfo data){
        return shipsmentInfoService.add(data);
    }

    @GetMapping("/advertId/{advertId}")
    public ShipsmentInfoDto advertStatusStep(@PathVariable int advertId){
        return  shipsmentInfoService.getTransportInfo(advertId);
    }
}
