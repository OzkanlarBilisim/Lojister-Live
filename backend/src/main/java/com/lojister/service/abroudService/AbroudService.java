package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.AdAbroud;

import java.util.List;

public interface AbroudService {
    public AdAbroud saveStudent(AdAbroud student);
    AdAbroud save(AdAbroud adAbroud);
    public List<AdAbroud> IdFind(int id);
    List<AdAbroud> getAllStudents();
    List<AdAbroud> getTEMPORARY_METHOD();
    public String advertStatusStep(int advertID);

}
