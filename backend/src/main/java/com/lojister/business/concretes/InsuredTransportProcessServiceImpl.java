package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.InsuredTransportProcessResponseDto;
import com.lojister.model.enums.DocumentUploadStatus;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.InsuredTransportProcessMapper;
import com.lojister.model.entity.InsuredTransportProcess;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.repository.transport.InsuredTransportProcessRepository;
import com.lojister.business.abstracts.InsuredTransportProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InsuredTransportProcessServiceImpl implements InsuredTransportProcessService {

    private final InsuredTransportProcessRepository insuredTransportProcessRepository;
    private final InsuredTransportProcessMapper insuredTransportProcessMapper;


    @Override
    public List<InsuredTransportProcessResponseDto> findWaiting() {
        return insuredTransportProcessMapper.entityListToDtoList(insuredTransportProcessRepository.findByDocumentUploadStatus(DocumentUploadStatus.WAITING));
    }

    @Override
    public InsuredTransportProcess findDataById(Long insuredTransportProcessId) {
        return insuredTransportProcessRepository.findById(insuredTransportProcessId).orElseThrow(() -> new EntityNotFoundException(Translator.toLocale("lojister.insuredTransportProces.EntityNotFoundException")));
    }

    @Override
    public void documentUploadSuccess(InsuredTransportProcess insuredTransportProcess) {
        insuredTransportProcessRepository.save(insuredTransportProcess.documentUploadSuccess());
    }

    @Override
    public void save(ClientTransportProcess clientTransportProcess) {
        insuredTransportProcessRepository.save(InsuredTransportProcess.create(clientTransportProcess));
    }


}
