package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.File;
import com.lojister.model.dto.abroudDto.FileDto;

import java.util.List;
import java.util.Optional;

public interface FileService {
    File saveFile(File file);
    File save(File file);

    List<FileDto> listFile(String role, int advert_ID);

    Optional<File> getFileById(Long id);
}
