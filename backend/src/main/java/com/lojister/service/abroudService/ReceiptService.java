package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.Receipt;
import com.lojister.model.dto.abroudDto.ReceitDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReceiptService {

    Receipt save(Receipt uploadedReceipt);
    Receipt findByID(Long id);

    void  approved(Long id, Boolean deleteOrAproved);
    ResponseEntity<byte[]> getFile(Long id);
    List<ReceitDto> findall(Boolean isReceipt);
}
