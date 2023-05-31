package com.lojister.controller.document;

import com.lojister.business.abstracts.DocumentService;
import com.lojister.core.helper.ContentTypeHelper;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.DocumentFileDto;
import com.lojister.model.dto.DriverDocumentFileDto;
import com.lojister.model.entity.DocumentFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.sql.SQLException;

@RestController
@RequestMapping("/document")
@CrossOrigin
@Authenticated
@RequiredArgsConstructor
public class DocumentController {

    private  final DocumentService documentService;

    @PostMapping("/transportProcessId/{transportProcessId}")
    public void saveTransportProcessDocumentFile(FileUploadUtil.FileResult result, @RequestBody SaveDocumentFileRequest saveDocumentFileRequest){

    }
    @GetMapping(value = "/{id}/url", produces = MediaType.ALL_VALUE)
    public ResponseEntity getDocumentUrl(@PathVariable Long id) throws SQLException {

        InputStream inputStream = documentService.getData(id).getBinaryStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
       /* String ext = FilenameUtils.getExtension(driverDocumentFileDto.getPath());
        String contentType = ContentTypeHelper.getDataByContentType(ext);*/

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.ALL_VALUE)
                .body(inputStreamResource);
        return entity;

    }

}
