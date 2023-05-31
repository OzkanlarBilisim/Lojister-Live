package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.DocumentsAbroud;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class AbroudDocumentsServiceImpl implements AbroudDocumentsService {

    @Autowired
    private AbroudDocumentsRepository studentRepository;
    @Autowired
    private AbroudBidService abroudBidService;
    @Autowired
    private  AbroudBidRepository abroudBidRepository;
    @Autowired AbroudService abroudService;


    @Override
    public DocumentsAbroud saveStudent(DocumentsAbroud student) {
        if(student.getDriverID() == 0){
            abroudBid  abroudBid2  = abroudBidRepository.findbidPaymentSuccessAproved(student.getTransportId()).get(0);

            student.setDriverID(abroudBid2.getCompanyId());
        }else {
            AdAbroud  adAbroud2   = abroudService.IdFind(student.getTransportId()).get(0);
            student.setClientID(Integer.parseInt(adAbroud2.getClient_id()));
        }

        Optional<DocumentsAbroud> documentsAbroudOption = studentRepository.findWantingAndTransportID(student.getWanting(), student.getTransportId());
        if(documentsAbroudOption.isPresent()){
            student.setId(documentsAbroudOption.get().getId());
        }
        return studentRepository.save(student);
    }

    @Override
    public List<DocumentsAbroud> getAllStudents() {
        return studentRepository.findAll();
    }
    @Override
    public Optional<DocumentsAbroud> advertIdWanting(int advertId, String wanting) {
        return studentRepository.findWantingAndTransportID(wanting, advertId);
    }

}
