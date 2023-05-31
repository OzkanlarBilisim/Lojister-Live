package com.lojister.repository.transport;

import com.lojister.model.entity.InsuredTransportProcessFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuredTransportProcessFileRepository extends JpaRepository<InsuredTransportProcessFile, Long> {

    Optional<InsuredTransportProcessFile> findByInsuredTransportProcess_ClientTransportProcess_TransportCode(String transportCode);

    Optional<InsuredTransportProcessFile> findByInsuredTransportProcess_Id(Long insuredTransportProcessId);

}
