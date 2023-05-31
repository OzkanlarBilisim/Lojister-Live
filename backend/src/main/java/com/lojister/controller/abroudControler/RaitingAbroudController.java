package com.lojister.controller.abroudControler;


import com.lojister.model.abroudModel.RaitingAbroud;
import com.lojister.model.dto.abroudDto.RatingDto;
import com.lojister.service.abroudService.RaitingAbroudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/RaitingAbroud")
public class RaitingAbroudController {

    @Autowired
    private RaitingAbroudService raitingAbroudService;

    @PostMapping("/add")
    public String add(@RequestBody RaitingAbroud raiting){
        return raitingAbroudService.save(raiting);
    }

    @GetMapping("/driver/{DriverId}")
    public List<RatingDto> getDriverRating(@PathVariable long DriverId){
        return raitingAbroudService.getDriverRating(DriverId);
    }


    @GetMapping("/getAll")
    public List<RaitingAbroud> getAllStudents(){
        return raitingAbroudService.getAllRaitings();
    }
}
