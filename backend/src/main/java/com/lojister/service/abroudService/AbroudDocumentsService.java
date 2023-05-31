package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.DocumentsAbroud;

import java.util.List;
import java.util.Optional;

public interface AbroudDocumentsService {
    public DocumentsAbroud saveStudent(DocumentsAbroud student);

    public List<DocumentsAbroud> getAllStudents();
    public Optional<DocumentsAbroud> advertIdWanting(int advertId, String wanting);
}
