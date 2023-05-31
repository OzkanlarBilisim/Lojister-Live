package com.lojister.repository.transport;

import com.lojister.model.enums.DocumentUploadStatus;
import com.lojister.model.entity.InsuredTransportProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuredTransportProcessRepository extends JpaRepository<InsuredTransportProcess, Long> {

    List<InsuredTransportProcess> findByDocumentUploadStatus(DocumentUploadStatus documentUploadStatus);

}
