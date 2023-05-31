package com.lojister.business.abstracts;

import com.lojister.model.dto.FileBase64Dto;
import com.lojister.model.dto.VehicleDocumentFileDto;
import com.lojister.model.dto.VehicleDocumentMinimalDto;
import com.lojister.model.dto.VehicleDocumentNotDataDto;
import com.lojister.model.entity.VehicleDocumentFile;
import com.lojister.core.util.FileUploadUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleDocumentFileService {

    VehicleDocumentFileDto getFileById(Long id);

    void deleteById(Long id);

    void deleteByIdList(List<Long> idList);

    void uploadFile(MultipartFile result, Long driverId, Long documentTypeId);

    VehicleDocumentNotDataDto getFileInfoById(Long id);

    List<VehicleDocumentNotDataDto> getFileInfoListById(List<Long> idList);

    FileBase64Dto getBase64VehicleDocumentFile(Long id);

    List<FileBase64Dto> getBase64VehicleDocumentFileList(List<Long> idList);

    List<VehicleDocumentMinimalDto> findMinimalVehicleDocumentByVehicleIdRepo(Long vehicleId);

    List<VehicleDocumentFile> findByVehicleIdRepo(Long id);

    VehicleDocumentFile findDataById(Long id);

}
