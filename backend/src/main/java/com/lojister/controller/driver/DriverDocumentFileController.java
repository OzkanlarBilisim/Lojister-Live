package com.lojister.controller.driver;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.DriverDocumentFileDto;
import com.lojister.model.dto.DriverDocumentNotDataDto;
import com.lojister.model.dto.FileBase64Dto;
import com.lojister.business.abstracts.DriverDocumentFileService;
import com.lojister.core.api.ApiPaths;
import com.lojister.core.helper.ContentTypeHelper;
import com.lojister.core.util.FileUploadUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;



@RestController
@RequestMapping("/driverDocument")
@CrossOrigin
@Authenticated
public class DriverDocumentFileController {

    private final DriverDocumentFileService driverDocumentFileService;

    public DriverDocumentFileController(DriverDocumentFileService driverDocumentFileService) {
        this.driverDocumentFileService = driverDocumentFileService;
    }


    @GetMapping(value = "/info/{id}")
    public ResponseEntity<DriverDocumentNotDataDto> getDriverDocumentInfo(@PathVariable(value = "id") Long id) {

        return ResponseEntity.ok(driverDocumentFileService.getFileInfoById(id));

    }

    @GetMapping(value = "/info/list")
    public ResponseEntity<List<DriverDocumentNotDataDto>> getDriverDocumentInfoList(@RequestBody List<Long> idList) {

        return ResponseEntity.ok(driverDocumentFileService.getFileInfoListById(idList));

    }

    @GetMapping(value = "/info/list/{userId}")
    public ResponseEntity<List<DriverDocumentNotDataDto>> getDriverDocumentInfoListByUserId(@PathVariable Long userId) {

        return ResponseEntity.ok(driverDocumentFileService.getFileInfoListByDriverId(userId));

    }
    @GetMapping(value = "/{id}/file", produces = MediaType.ALL_VALUE)
    public ResponseEntity getDriverDocumentFile(@PathVariable Long id) throws SQLException {
        DriverDocumentFileDto driverDocumentFileDto = driverDocumentFileService.getFileById(id);

        InputStream inputStream = driverDocumentFileDto.getData().getBinaryStream();

        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + driverDocumentFileDto.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
        return entity;
    }

    @GetMapping(value = "/{id}/url", produces = MediaType.ALL_VALUE)
    public ResponseEntity getDriverDocumentUrl(@PathVariable Long id) throws SQLException {

        DriverDocumentFileDto driverDocumentFileDto = driverDocumentFileService.getFileById(id);

        InputStream inputStream = driverDocumentFileDto.getData().getBinaryStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        String ext = FilenameUtils.getExtension(driverDocumentFileDto.getPath());
        String contentType = ContentTypeHelper.getDataByContentType(ext);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(inputStreamResource);
        return entity;
    }

    @GetMapping(value = "/base64/{id}")
    public ResponseEntity<FileBase64Dto> getBase64DriverDocumentFile(@PathVariable Long id) {

        return ResponseEntity.ok(driverDocumentFileService.getBase64DriverDocumentFile(id));

    }

    @PostMapping(value = "/base64/list")
    public ResponseEntity<List<FileBase64Dto>> getBase64DriverDocumentFileList(@RequestBody List<Long> idList) {

        return ResponseEntity.ok(driverDocumentFileService.getBase64DriverDocumentFileList(idList));

    }

    @PostMapping("/upload")
    public void uploadFile(MultipartFile result, @RequestParam(name = "driverId") Long driverId, @RequestParam(name = "documentTypeId") Long documentTypeId) {
        driverDocumentFileService.uploadFile(result, driverId, documentTypeId);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteDriverFileById(@PathVariable(value = "id") Long id) {

        driverDocumentFileService.deleteById(id);
    }


    @DeleteMapping(value = "/list")
    public void deleteDriverFilesByIdList(@RequestBody List<Long> idList) {

        driverDocumentFileService.deleteByIdList(idList);
    }


}
