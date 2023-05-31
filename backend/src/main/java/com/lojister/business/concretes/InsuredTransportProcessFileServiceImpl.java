package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.enums.DocumentUploadStatus;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.model.entity.InsuredTransportProcess;
import com.lojister.model.entity.InsuredTransportProcessFile;
import com.lojister.repository.transport.InsuredTransportProcessFileRepository;
import com.lojister.repository.transport.InsuredTransportProcessRepository;
import com.lojister.business.abstracts.ClientTransportProcessService;
import com.lojister.business.abstracts.InsuredTransportProcessFileService;
import com.lojister.business.abstracts.InsuredTransportProcessService;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.TempFileUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Blob;

@Service
@Transactional
@RequiredArgsConstructor
public class InsuredTransportProcessFileServiceImpl implements InsuredTransportProcessFileService {

    private final InsuredTransportProcessFileRepository insuredTransportProcessFileRepository;
    private final InsuredTransportProcessService insuredTransportProcessService;
    private final InsuredTransportProcessRepository insuredTransportProcessRepository;
    private final ClientTransportProcessService clientTransportProcessService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void duplicateFileCheck(InsuredTransportProcess insuredTransportProcess) {

        if (insuredTransportProcess.getDocumentUploadStatus() == DocumentUploadStatus.SUCCESSFUL) {
            insuredTransportProcessFileRepository.findByInsuredTransportProcess_Id(insuredTransportProcess.getId()).ifPresent(insuredTransportProcessFileRepository::delete);
            insuredTransportProcessRepository.save(insuredTransportProcess.documentUploadSetStatusWaiting());
        }

    }

    @Override
    public void uploadFile(FileUploadUtil.FileResult result, Long insuredTransportProcessId) {

        InsuredTransportProcess insuredTransportProcess = insuredTransportProcessService.findDataById(insuredTransportProcessId);

        duplicateFileCheck(insuredTransportProcess);

        try (TempFileUtil.TempFileResult tempFileResult = TempFileUtil.cache(result.getFilename(), result.getFileStream())) {
            Blob blob = BlobProxy.generateProxy(tempFileResult.getFileInputStream(), tempFileResult.getLength());

            InsuredTransportProcessFile insuredTransportProcessFile = InsuredTransportProcessFile.builder()
                    .insuredTransportProcess(insuredTransportProcess)
                    .fileName(result.getFilename())
                    .data(blob)
                    .build();

            insuredTransportProcessFileRepository.save(insuredTransportProcessFile);
            insuredTransportProcessService.documentUploadSuccess(insuredTransportProcess);
            clientTransportProcessService.changeStatusTrueAfterUploadingTheFile(insuredTransportProcess.getClientTransportProcess());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public InsuredTransportProcessFile findDataByClientTransportProcessTransportCode(String transportCode) {
        return insuredTransportProcessFileRepository.findByInsuredTransportProcess_ClientTransportProcess_TransportCode(transportCode).orElseThrow(() -> new EntityNotFoundException(Translator.toLocale("lojister.insuredTransportProcesFile.EntityNotFoundException")));
    }


}
