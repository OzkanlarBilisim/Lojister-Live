package com.lojister.controller.abroudControler;


import com.lojister.model.abroudModel.DocumentsAbroud;
import com.lojister.model.dto.abroudDto.BidAndAdvertRequestDto;
import com.lojister.service.abroudService.AbroudDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/DocumentsAbroud")
@CrossOrigin
public class AbroudDocumentsController {

    @Autowired
    private AbroudDocumentsService studentService;

    @PostMapping("/add")
    public String add(@RequestBody DocumentsAbroud student){
        studentService.saveStudent(student);
        return "Yeni ğrenci eklendi";
    }

    @GetMapping("/advertId/{advertId}/wanting/{wanting}")
    public Optional<DocumentsAbroud> advertIdWanting(@PathVariable int advertId, @PathVariable String wanting){
        return studentService.advertIdWanting(advertId, wanting);
    }


    @GetMapping("/getAll")
    public List<DocumentsAbroud> getAllStudents(){
        return studentService.getAllStudents();
    }
}
