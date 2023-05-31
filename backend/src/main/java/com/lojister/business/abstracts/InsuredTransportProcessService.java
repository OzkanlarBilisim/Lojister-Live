package com.lojister.business.abstracts;

import com.lojister.model.dto.InsuredTransportProcessResponseDto;
import com.lojister.model.entity.InsuredTransportProcess;
import com.lojister.model.entity.client.ClientTransportProcess;

import java.util.List;

public interface InsuredTransportProcessService {

    List<InsuredTransportProcessResponseDto> findWaiting();
    InsuredTransportProcess findDataById(Long insuredTransportProcessId);
    void documentUploadSuccess(InsuredTransportProcess insuredTransportProcess);
    void save(ClientTransportProcess clientTransportProcess);
}
