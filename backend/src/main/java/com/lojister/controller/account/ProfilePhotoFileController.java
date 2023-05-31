package com.lojister.controller.account;


import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.FileBase64Dto;
import com.lojister.model.dto.ProfilePhotoFileDto;
import com.lojister.business.abstracts.ProfilePhotoFileService;
import com.lojister.core.api.ApiPaths;
import com.lojister.core.helper.ContentTypeHelper;
import com.lojister.core.util.FileUploadUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.SQLException;

@RestController
@RequestMapping("/profilePhotoFile")
@CrossOrigin
@Authenticated
public class ProfilePhotoFileController {

    private final ProfilePhotoFileService profilePhotoService;

    @Autowired
    public ProfilePhotoFileController(ProfilePhotoFileService profilePhotoService) {
        this.profilePhotoService = profilePhotoService;
    }


    @PostMapping("/upload")
    public void uploadFile(MultipartFile result) {

        profilePhotoService.uploadFile(result);
    }

    @GetMapping(value = "/my/base64")
    public ResponseEntity<FileBase64Dto> getMyProfilePhotoBase64() {

        return ResponseEntity.ok(profilePhotoService.getMyProfilePhotoBase64());
    }
    @GetMapping(value = "/user/{userId}/base64")
    public ResponseEntity<FileBase64Dto> getMyProfilePhotoBase64(@PathVariable("userId") Long userId) {

        return ResponseEntity.ok(profilePhotoService.getUserProfilePhotoBase64(userId));
    }

    @DeleteMapping(value = "/my")
    public void deleteMyProfilePhoto() {
        profilePhotoService.deleteMyPhoto();
    }


    @GetMapping(value = "/{id}/file", produces = MediaType.ALL_VALUE)
    @OnlyAdmin
    public ResponseEntity getProfilePhotoFile(@PathVariable Long id) throws SQLException {

        ProfilePhotoFileDto profilePhotoFileDto = profilePhotoService.getFileById(id);

        InputStream inputStream = profilePhotoFileDto.getData().getBinaryStream();

        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + profilePhotoFileDto.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
        return entity;
    }


    @GetMapping(value = "/{id}/url", produces = MediaType.ALL_VALUE)
    @OnlyAdmin
    public ResponseEntity getProfilePhotoUrl(@PathVariable Long id) throws SQLException {

        ProfilePhotoFileDto profilePhotoFileDto = profilePhotoService.getFileById(id);

        InputStream inputStream = profilePhotoFileDto.getData().getBinaryStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        String ext = FilenameUtils.getExtension(profilePhotoFileDto.getPath());
        String contentType = ContentTypeHelper.getDataByContentType(ext);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(inputStreamResource);
        return entity;
    }

    @GetMapping(value = "/my/file", produces = MediaType.ALL_VALUE)
    public ResponseEntity getMyProfilePhotoFile() throws SQLException {


        ProfilePhotoFileDto profilePhotoFileDto = profilePhotoService.getMyFile();

        InputStream inputStream = profilePhotoFileDto.getData().getBinaryStream();

        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + profilePhotoFileDto.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
        return entity;
    }


    @GetMapping(value = "/my/url", produces = MediaType.ALL_VALUE)
    public ResponseEntity getMyProfilePhotoUrl() throws SQLException {

        ProfilePhotoFileDto profilePhotoFileDto = profilePhotoService.getMyFile();

        InputStream inputStream = profilePhotoFileDto.getData().getBinaryStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        String ext = FilenameUtils.getExtension(profilePhotoFileDto.getPath());
        String contentType = ContentTypeHelper.getDataByContentType(ext);

        ResponseEntity entity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(inputStreamResource);
        return entity;
    }


}
