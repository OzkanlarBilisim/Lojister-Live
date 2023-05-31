package com.lojister.business.abstracts;

import com.lojister.model.dto.FileBase64Dto;
import com.lojister.model.dto.ProfilePhotoFileDto;
import com.lojister.core.util.FileUploadUtil;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePhotoFileService {

    ProfilePhotoFileDto getFileById(Long id);

    void deleteById(Long id);

    void deleteMyPhoto();

    ProfilePhotoFileDto getMyFile();

    void uploadFile(MultipartFile result);

    FileBase64Dto getMyProfilePhotoBase64();
    FileBase64Dto getUserProfilePhotoBase64(Long userId);

}
