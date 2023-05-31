package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.FileBase64Dto;
import com.lojister.model.dto.ProfilePhotoFileDto;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.exception.PhotoExceptionTest;
import com.lojister.mapper.ProfilePhotoFileMapper;
import com.lojister.model.entity.ProfilePhotoFile;
import com.lojister.model.entity.User;
import com.lojister.repository.account.ProfilePhotoFileRepository;
import com.lojister.business.abstracts.ProfilePhotoFileService;
import com.lojister.core.helper.ContentTypeHelper;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.util.TempFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class ProfilePhotoServiceImpl implements ProfilePhotoFileService {

    private final ProfilePhotoFileRepository profilePhotoFileRepository;
    private final ProfilePhotoFileMapper profilePhotoFileMapper;
    private final SecurityContextUtil securityContextUtil;

    private static final String PROFILE_PHOTO_NOTFOUND_MESSAGE = "Profil fotoğrafı bulunamadı.";
    private static final String PROFILE_PHOTO_NOTFOUND_MESSAGE_EN = "Profile picture cannot found.";



    //TODO BUNU TEKRARDAN TEST ET.
    @Override
    public void uploadFile(MultipartFile file) {

        User user = securityContextUtil.getCurrentUser();

        Optional<ProfilePhotoFile> profilePhotoFileOptional = profilePhotoFileRepository.findByUser_Id(user.getId());

        if (profilePhotoFileOptional.isPresent()) {
            profilePhotoFileRepository.deleteById(profilePhotoFileOptional.get().getId());
        }

        ProfilePhotoFile profilePhotoFile = new ProfilePhotoFile();

        try {
            Blob blob = BlobProxy.generateProxy(file.getInputStream(), file.getSize());
            profilePhotoFile.setData(blob);
            profilePhotoFile.setFileName(file.getOriginalFilename());
            profilePhotoFile.setUser(user);
            profilePhotoFileRepository.save(profilePhotoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteById(Long id) {

        Optional<ProfilePhotoFile> profilePhotoFile = profilePhotoFileRepository.findById(id);

        if (profilePhotoFile.isPresent()) {

            profilePhotoFileRepository.deleteById(id);

        } else {

            throw new EntityNotFoundException(Translator.toLocale("lojister.profilePhoto.EntityNotFoundException"));
        }
    }


    @Override
    public void deleteMyPhoto() {

        Optional<ProfilePhotoFile> profilePhotoFile = profilePhotoFileRepository.findByUser_Id(securityContextUtil.getCurrentUserId());

        if (profilePhotoFile.isPresent()) {

            profilePhotoFileRepository.deleteById(profilePhotoFile.get().getId());

        } else {

            throw new EntityNotFoundException(Translator.toLocale("lojister.profilePhoto.EntityNotFoundException"));

        }
    }


    @Override
    public ProfilePhotoFileDto getFileById(Long id) {

        Optional<ProfilePhotoFile> profilePhotoFile = profilePhotoFileRepository.findById(id);

        if (profilePhotoFile.isPresent()) {

            return profilePhotoFileMapper.entityToDto(profilePhotoFile.get());
        } else {

            throw new EntityNotFoundException(Translator.toLocale("lojister.profilePhoto.EntityNotFoundException"));
        }
    }

    @Override
    public ProfilePhotoFileDto getMyFile() {

        Optional<ProfilePhotoFile> profilePhotoFile = profilePhotoFileRepository.findByUser_Id(securityContextUtil.getCurrentUserId());

        if (profilePhotoFile.isPresent()) {

            return profilePhotoFileMapper.entityToDto(profilePhotoFile.get());
        } else {

            throw new EntityNotFoundException(Translator.toLocale("lojister.profilePhoto.EntityNotFoundException"));
        }
    }

    //TODO BURAYA DA BAK SONRADAN.
    @Override
    public FileBase64Dto getMyProfilePhotoBase64() {

        Optional<ProfilePhotoFile> profilePhotoFile = profilePhotoFileRepository.findByUser_Id(securityContextUtil.getCurrentUserId());

        if (profilePhotoFile.isPresent()) {

            FileBase64Dto fileBase64Dto = new FileBase64Dto();

            String ext = FilenameUtils.getExtension(profilePhotoFile.get().getFileName());
            String contentType = ContentTypeHelper.getDataByContentType(ext);

            try {
                InputStream is = profilePhotoFile.get().getData().getBinaryStream();
                byte[] bytes = IOUtils.toByteArray(is);
                String encoded = Base64.encodeBase64String(bytes);
                fileBase64Dto.setData(encoded);
                fileBase64Dto.setType(contentType);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            return fileBase64Dto;

        } else {

            throw new PhotoExceptionTest();
            // throw new EntityNotFoundException("Profile photo Bulunamadi");
        }
    }
    @Override
    public FileBase64Dto getUserProfilePhotoBase64(Long userId) {

        Optional<ProfilePhotoFile> profilePhotoFile = profilePhotoFileRepository.findByUser_Id(userId);

        if (profilePhotoFile.isPresent()) {

            FileBase64Dto fileBase64Dto = new FileBase64Dto();

            String ext = FilenameUtils.getExtension(profilePhotoFile.get().getFileName());
            String contentType = ContentTypeHelper.getDataByContentType(ext);

            try {
                InputStream is = profilePhotoFile.get().getData().getBinaryStream();
                byte[] bytes = IOUtils.toByteArray(is);
                String encoded = Base64.encodeBase64String(bytes);
                fileBase64Dto.setData(encoded);
                fileBase64Dto.setType(contentType);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            return fileBase64Dto;

        } else {

            throw new PhotoExceptionTest();
            // throw new EntityNotFoundException("Profile photo Bulunamadi");
        }
    }


}

