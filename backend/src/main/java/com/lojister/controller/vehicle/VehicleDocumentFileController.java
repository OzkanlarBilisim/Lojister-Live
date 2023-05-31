package com.lojister.controller.vehicle;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.*;
import com.lojister.business.abstracts.VehicleDocumentFileService;
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
@RequestMapping("/vehicleDocument")
@CrossOrigin
@Authenticated
public class VehicleDocumentFileController {

    private final VehicleDocumentFileService vehicleDocumentFileService;

    public VehicleDocumentFileController(VehicleDocumentFileService vehicleDocumentFileService) {
        this.vehicleDocumentFileService = vehicleDocumentFileService;
    }


    @GetMapping(value = "/info/{id}")
    public ResponseEntity<VehicleDocumentNotDataDto> getPhotoInfo(@PathVariable(value = "id") Long id) {

        return ResponseEntity.ok(vehicleDocumentFileService.getFileInfoById(id));
    }
    @GetMapping(value = "/info/list/{vehicleId}")
    public ResponseEntity<List<VehicleDocumentMinimalDto>> findMinimalVehicleDocumentByVehicleIdRepo(@PathVariable Long vehicleId) {

        return ResponseEntity.ok(vehicleDocumentFileService.findMinimalVehicleDocumentByVehicleIdRepo(vehicleId));

    }
    @GetMapping(value = "/info/list")
    public ResponseEntity<List<VehicleDocumentNotDataDto>> getPhotosInfo(@RequestBody List<Long> idList) {

        return ResponseEntity.ok(vehicleDocumentFileService.getFileInfoListById(idList));
    }

    @GetMapping(value = "/{id}/file", produces = MediaType.ALL_VALUE)
    public ResponseEntity getDriverDocumentFile(@PathVariable Long id) throws SQLException {

        VehicleDocumentFileDto vehicleDocumentFileDto = vehicleDocumentFileService.getFileById(id);

        InputStream inputStream = vehicleDocumentFileDto.getData().getBinaryStream();

        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + vehicleDocumentFileDto.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
        return entity;
    }


    @GetMapping(value = "/{id}/url", produces = MediaType.ALL_VALUE)
    public ResponseEntity getDriverDocumentUrl(@PathVariable Long id) throws SQLException {

        VehicleDocumentFileDto vehicleDocumentFileDto = vehicleDocumentFileService.getFileById(id);

        InputStream inputStream = vehicleDocumentFileDto.getData().getBinaryStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        String ext = FilenameUtils.getExtension(vehicleDocumentFileDto.getPath());
        String contentType = ContentTypeHelper.getDataByContentType(ext);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(inputStreamResource);
        return entity;
    }

    @PostMapping("/upload")
    public void uploadFile(MultipartFile result, @RequestParam(name = "vehicleId") Long vehicleId, @RequestParam(name = "documentTypeId") Long documentTypeId) {

        vehicleDocumentFileService.uploadFile(result, vehicleId, documentTypeId);
    }


    @GetMapping(value = "/base64/{id}")
    public ResponseEntity<FileBase64Dto> getBase64VehicleDocumentFile(@PathVariable Long id) {

        return ResponseEntity.ok(vehicleDocumentFileService.getBase64VehicleDocumentFile(id));

    }

    @GetMapping(value = "/base64/list")
    public ResponseEntity<List<FileBase64Dto>> getBase64VehicleDocumentFileList(@RequestBody List<Long> idList) {

        return ResponseEntity.ok(vehicleDocumentFileService.getBase64VehicleDocumentFileList(idList));
    }


    @DeleteMapping(value = "/{id}")
    public void deleteDriverFileById(@PathVariable(value = "id") Long id) {

        vehicleDocumentFileService.deleteById(id);
    }

    @DeleteMapping(value = "/list")
    public void deleteDriverFilesByIdList(@RequestBody List<Long> idList) {

        vehicleDocumentFileService.deleteByIdList(idList);
    }


}
