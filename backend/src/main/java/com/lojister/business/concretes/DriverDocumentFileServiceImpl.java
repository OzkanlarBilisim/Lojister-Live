package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.DriverDocumentFileDto;
import com.lojister.model.dto.DriverDocumentNotDataDto;
import com.lojister.model.dto.FileBase64Dto;
import com.lojister.model.enums.DriverStatus;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.DriverDocumentFileMapper;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.entity.driver.DriverDocumentFile;
import com.lojister.model.entity.adminpanel.DriverDocumentType;
import com.lojister.repository.driver.DriverDocumentFileRepository;
import com.lojister.business.abstracts.DriverDocumentFileService;
import com.lojister.business.abstracts.dynamic.DriverDocumentTypeService;
import com.lojister.business.abstracts.DriverService;
import com.lojister.core.helper.ContentTypeHelper;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.TempFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.engine.jdbc.BlobProxy;
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
@RequiredArgsConstructor
public class DriverDocumentFileServiceImpl implements DriverDocumentFileService {

    private final DriverDocumentFileRepository driverDocumentFileRepository;
    private final DriverDocumentTypeService driverDocumentTypeService;
    private final DriverService driverService;
    private final DriverDocumentFileMapper driverDocumentFileMapper;


    @Override
    public void uploadFile(MultipartFile multipartFile, Long driverId, Long documentTypeId) {

        DriverDocumentFile driverDocumentFile = new DriverDocumentFile();
        Driver driver = driverService.findDataById(driverId);
        DriverDocumentType driverDocumentType = driverDocumentTypeService.findDataById(documentTypeId);

        try {

            Blob blob = BlobProxy.generateProxy(multipartFile.getInputStream(), multipartFile.getSize());
            driverDocumentFile.setData(blob);
            driverDocumentFile.setFileName(multipartFile.getOriginalFilename());
            driverDocumentFile.setDriver(driver);
            driverDocumentFile.setDriverDocumentType(driverDocumentType);
            driverDocumentFileRepository.save(driverDocumentFile);

            if (driver.getStatus() == DriverStatus.REGISTERED) {
                driver.setStatus(DriverStatus.REVIEW);
            } else if (driver.getStatus() == DriverStatus.REVIEW_SENT) {
                driver.setStatus(DriverStatus.REVISION);
            }
            driverService.saveRepo(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public List<DriverDocumentNotDataDto> getFileInfoListByDriverId(Long driverId) {

        return driverDocumentFileRepository.findNoDataByDriverId(driverId);
    }

    @Override
    public DriverDocumentNotDataDto getFileInfoById(Long id) {

        return driverDocumentFileRepository.findNoDataById(id);
    }

    @Override
    public List<DriverDocumentNotDataDto> getFileInfoListById(List<Long> idList) {

        ArrayList<DriverDocumentNotDataDto> fileInfoList = new ArrayList<>();

        for (Long id : idList) {

            DriverDocumentNotDataDto driverDocumentNotDataDto = driverDocumentFileRepository.findNoDataById(id);

            if (driverDocumentNotDataDto == null) {
                throw new EntityNotFoundException(Translator.toLocale("lojister.driverDocumentFile.EntityNotFoundException"));
            }

            fileInfoList.add(driverDocumentNotDataDto);
        }
        return fileInfoList;
    }

    @Override
    public void deleteById(Long id) {

        findDataById(id);
        driverDocumentFileRepository.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {

        for (Long id : idList) {

            findDataById(id);
            driverDocumentFileRepository.deleteById(id);
        }
    }

    @Override
    public DriverDocumentFileDto getFileById(Long id) {

        DriverDocumentFile driverDocumentFile = findDataById(id);

        return driverDocumentFileMapper.entityToDto(driverDocumentFile);
    }

    @Override
    public FileBase64Dto getBase64DriverDocumentFile(Long id) {

        DriverDocumentFile driverDocumentFile = findDataById(id);

        FileBase64Dto fileBase64Dto = new FileBase64Dto();

        String ext = FilenameUtils.getExtension(driverDocumentFile.getFileName());
        String contentType = ContentTypeHelper.getDataByContentType(ext);

        try {

            InputStream is = driverDocumentFile.getData().getBinaryStream();
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
    public List<FileBase64Dto> getBase64DriverDocumentFileList(List<Long> idList) {

        List<FileBase64Dto> base64DtoList = new ArrayList<>();

        for (Long id : idList) {

            DriverDocumentFile driverDocumentFile = findDataById(id);

            FileBase64Dto fileBase64Dto = new FileBase64Dto();

            String ext = FilenameUtils.getExtension(driverDocumentFile.getFileName());
            String contentType = ContentTypeHelper.getDataByContentType(ext);

            try {
                InputStream is = driverDocumentFile.getData().getBinaryStream();
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
    public DriverDocumentFile findDataById(Long id) {

        Optional<DriverDocumentFile> driverDocumentFileOptional = driverDocumentFileRepository.findById(id);

        if (driverDocumentFileOptional.isPresent()) {

            return driverDocumentFileOptional.get();
        } else {

            throw new EntityNotFoundException(Translator.toLocale("lojister.driverDocumentFile.EntityNotFoundException"));
        }
    }


}
