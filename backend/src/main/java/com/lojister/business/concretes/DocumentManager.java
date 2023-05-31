package com.lojister.business.concretes;

import com.lojister.business.abstracts.DocumentService;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.model.dto.DocumentFileDto;
import com.lojister.model.dto.DocumentFileWithoutDataDto;
import com.lojister.model.entity.DocumentFile;
import com.lojister.model.enums.DocumentType;
import com.lojister.repository.document.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Blob;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentManager implements DocumentService {
    private final DocumentRepository documentRepository;

    @Override
    public DocumentFile findTransportProcessIdAndUserId(Long userId, Long clientTransportProcessId) {
        return documentRepository.findByClientTransportProcess_IdAndUser_Id(clientTransportProcessId,userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(DocumentFile documentFile) {
        documentRepository.save(documentFile);
    }

    @Override
    public Blob getData(Long id) {
        return documentRepository.findByIdOnlyData(id).orElseThrow(EntityNotFoundException::new).getData();
    }

    @Override
    public Boolean isDocumentType(Long clientTransportProcessId, DocumentType documentType) {
        return documentRepository.existsByClientTransportProcess_IdAndDocumentType(clientTransportProcessId,documentType);
    }

    @Override
    public List<DocumentFileWithoutDataDto> findTransportProcessId(Long clientTransportProcessId) {
        return documentRepository.findByClientTransportProcess_Id(clientTransportProcessId);
    }


}
