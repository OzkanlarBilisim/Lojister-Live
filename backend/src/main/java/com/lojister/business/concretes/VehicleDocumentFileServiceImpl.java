package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.abroudModel.File;
import com.lojister.model.dto.FileBase64Dto;
import com.lojister.model.dto.VehicleDocumentFileDto;
import com.lojister.model.dto.VehicleDocumentMinimalDto;
import com.lojister.model.dto.VehicleDocumentNotDataDto;
import com.lojister.model.enums.VehicleStatus;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.VehicleDocumentFileMapper;
import com.lojister.model.entity.Vehicle;
import com.lojister.model.entity.VehicleDocumentFile;
import com.lojister.model.entity.adminpanel.VehicleDocumentType;
import com.lojister.repository.vehicle.VehicleDocumentFileRepository;
import com.lojister.business.abstracts.VehicleDocumentFileService;
import com.lojister.business.abstracts.dynamic.VehicleDocumentTypeService;
import com.lojister.business.abstracts.VehicleService;
import com.lojister.core.helper.ContentTypeHelper;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.TempFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class VehicleDocumentFileServiceImpl implements VehicleDocumentFileService {

    private final VehicleDocumentFileRepository vehicleDocumentFileRepository;
    private final VehicleDocumentTypeService vehicleDocumentTypeService;
    @Lazy
    private final VehicleService vehicleService;
    private final VehicleDocumentFileMapper vehicleDocumentFileMapper;




    @Override
    public void uploadFile(MultipartFile file, Long vehicleId, Long documentTypeId) {

        VehicleDocumentFile vehicleDocumentFile = new VehicleDocumentFile();
        Vehicle vehicle = vehicleService.findDataById(vehicleId);
        VehicleDocumentType vehicleDocumentType = vehicleDocumentTypeService.findDataById(documentTypeId);

        try {
            Blob blob = BlobProxy.generateProxy(file.getInputStream(), file.getSize());

            if (vehicle.getVehicleStatus() == VehicleStatus.REGISTERED) {
                vehicle.setVehicleStatus(VehicleStatus.REVIEW);
            } else if (vehicle.getVehicleStatus() == VehicleStatus.REVIEW_SENT) {
                vehicle.setVehicleStatus(VehicleStatus.REVISION);
            }

            vehicleDocumentFile.setData(blob);
            vehicleDocumentFile.setFileName(file.getOriginalFilename());
            vehicleDocumentFile.setVehicle(vehicle);
            vehicleDocumentFile.setVehicleDocumentType(vehicleDocumentType);
            vehicleDocumentFileRepository.save(vehicleDocumentFile);

            vehicleService.saveRepo(vehicle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public VehicleDocumentNotDataDto getFileInfoById(Long id) {

        return vehicleDocumentFileRepository.findNoDataById(id);
    }

    @Override
    public List<VehicleDocumentNotDataDto> getFileInfoListById(List<Long> idList) {

        ArrayList<VehicleDocumentNotDataDto> fileInfoList = new ArrayList<>();

        for (Long id : idList) {

            VehicleDocumentNotDataDto vehicleDocumentNotDataDto = vehicleDocumentFileRepository.findNoDataById(id);

            if (vehicleDocumentNotDataDto == null) {
                throw new EntityNotFoundException(Translator.toLocale("lojister.vehicleDocumentFile.EntityNotFoundException"));
            }

            fileInfoList.add(vehicleDocumentNotDataDto);
        }
        return fileInfoList;
    }

    @Override
    public void deleteById(Long id) {

        findDataById(id);
        vehicleDocumentFileRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByIdList(List<Long> idList) {

        for (Long id : idList) {

            findDataById(id);
            vehicleDocumentFileRepository.deleteById(id);
        }
    }

    @Override
    public VehicleDocumentFileDto getFileById(Long id) {

        VehicleDocumentFile vehicleDocumentFile = findDataById(id);

        return vehicleDocumentFileMapper.entityToDto(vehicleDocumentFile);

    }


    @Override
    public FileBase64Dto getBase64VehicleDocumentFile(Long id) {

        VehicleDocumentFile vehicleDocumentFile = findDataById(id);

        FileBase64Dto fileBase64Dto = new FileBase64Dto();

        String ext = FilenameUtils.getExtension(vehicleDocumentFile.getFileName());
        String contentType = ContentTypeHelper.getDataByContentType(ext);

        try {
            InputStream is = vehicleDocumentFile.getData().getBinaryStream();
            byte[] bytes = IOUtils.toByteArray(is);
            String encoded = Base64.encodeBase64String(bytes);
            fileBase64Dto.setData(encoded);
            fileBase64Dto.setType(contentType);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return fileBase64Dto;
    }


    @Override
    public List<FileBase64Dto> getBase64VehicleDocumentFileList(List<Long> idList) {

        List<FileBase64Dto> base64DtoList = new ArrayList<>();

        for (Long id : idList) {

            VehicleDocumentFile vehicleDocumentFile = findDataById(id);

            FileBase64Dto fileBase64Dto = new FileBase64Dto();

            String ext = FilenameUtils.getExtension(vehicleDocumentFile.getFileName());
            String contentType = ContentTypeHelper.getDataByContentType(ext);

            try {
                InputStream is = vehicleDocumentFile.getData().getBinaryStream();
                byte[] bytes = IOUtils.toByteArray(is);
                String encoded = Base64.encodeBase64String(bytes);
                fileBase64Dto.setData(encoded);
                fileBase64Dto.setType(contentType);
                base64DtoList.add(fileBase64Dto);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return base64DtoList;
    }

    @Override
    public List<VehicleDocumentMinimalDto> findMinimalVehicleDocumentByVehicleIdRepo(Long vehicleId) {

        return vehicleDocumentFileRepository.findMinimalVehicleDocumentByVehicleId(vehicleId);
    }

    @Override
    public List<VehicleDocumentFile> findByVehicleIdRepo(Long id) {
        return vehicleDocumentFileRepository.findByVehicle_Id(id);
    }

    @Override
    public VehicleDocumentFile findDataById(Long id) {

        Optional<VehicleDocumentFile> vehicleDocumentFileOptional = vehicleDocumentFileRepository.findById(id);
        if (vehicleDocumentFileOptional.isPresent()) {
            return vehicleDocumentFileOptional.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.vehicleDocumentFile.EntityNotFoundException"));
        }
    }


}

