package com.lojister.repository.document;

import com.lojister.model.dto.DocumentFileWithoutDataDto;
import com.lojister.model.entity.DocumentFile;
import com.lojister.model.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentFile,Long> {
    Optional<DocumentFile> findByClientTransportProcess_IdAndUser_Id(Long transportProcessId,Long userId);
    @Query(value = "select df from #{#entityName} df where df.id=:id")
    Optional<DocumentFile> findByIdOnlyData(@Param(value = "id") Long id);

    @Query("select new com.lojister.model.dto.DocumentFileWithoutDataDto(df.id, df.fileName, df.contentType,df.user, df.documentType) from #{#entityName} df where df.clientTransportProcess.id =:transportProcessId")
    List<DocumentFileWithoutDataDto> findByClientTransportProcess_Id(@Param("transportProcessId") Long transportProcessId);

    Boolean existsByClientTransportProcess_IdAndDocumentType(Long transportProcessId, DocumentType documentType);
}
