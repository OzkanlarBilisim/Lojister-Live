package com.lojister.controller.transport;


import com.lojister.core.util.annotation.Admin_Client_InsuranceCompany;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.IsInsuranceCompany;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.entity.InsuredTransportProcessFile;
import com.lojister.business.abstracts.InsuredTransportProcessFileService;
import com.lojister.core.util.FileUploadUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.sql.SQLException;

@RestController
@RequestMapping("/transportProcess/insured/file")
@CrossOrigin
@Authenticated
public class InsuredTransportProcessFileController {

    private final InsuredTransportProcessFileService insuredTransportProcessFileService;

    public InsuredTransportProcessFileController(InsuredTransportProcessFileService insuredTransportProcessFileService) {
        this.insuredTransportProcessFileService = insuredTransportProcessFileService;
    }

    @PostMapping("/upload")
    @IsInsuranceCompany
    public void uploadFile(FileUploadUtil.FileResult result, @RequestParam(name = "insuredTransportProcessId") Long insuredTransportProcessId) {
        insuredTransportProcessFileService.uploadFile(result, insuredTransportProcessId);
    }

    @PermitAllCustom
    @GetMapping(value = "/download", produces = MediaType.ALL_VALUE)
    @Admin_Client_InsuranceCompany
    public ResponseEntity downloadInsuranceFileByTransportCode(@RequestParam(value = "transportCode") String transportCode) throws SQLException {

        InsuredTransportProcessFile insuredTransportProcessFile = insuredTransportProcessFileService.findDataByClientTransportProcessTransportCode(transportCode);
/*
        InputStream inputStream = insuredTransportProcessFile.getData().getBinaryStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE)
                .body(inputStreamResource);
        return entity;
  */
        InputStream inputStream = insuredTransportProcessFile.getData().getBinaryStream();

        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + insuredTransportProcessFile.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
  }



}
