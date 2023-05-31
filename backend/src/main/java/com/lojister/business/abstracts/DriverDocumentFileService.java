package com.lojister.business.abstracts;

import com.lojister.model.dto.FileBase64Dto;
import com.lojister.model.dto.DriverDocumentFileDto;
import com.lojister.model.dto.DriverDocumentNotDataDto;
import com.lojister.model.entity.driver.DriverDocumentFile;
import com.lojister.core.util.FileUploadUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DriverDocumentFileService {

    DriverDocumentFileDto getFileById(Long id);

    void deleteById(Long id);

    void deleteByIdList(List<Long> idList);

    void uploadFile(MultipartFile result, Long driverId, Long documentTypeId);

    FileBase64Dto getBase64DriverDocumentFile(Long id);

    List<FileBase64Dto> getBase64DriverDocumentFileList(List<Long> idList);

    DriverDocumentNotDataDto getFileInfoById(Long id);

    List<DriverDocumentNotDataDto> getFileInfoListById(List<Long> idList);

    List<DriverDocumentNotDataDto> getFileInfoListByDriverId(Long driverId);

    DriverDocumentFile findDataById(Long id);

}
