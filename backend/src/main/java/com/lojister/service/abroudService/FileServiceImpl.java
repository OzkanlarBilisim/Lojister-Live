package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.File;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.dto.abroudDto.DriverBidList;
import com.lojister.model.dto.abroudDto.FileDto;
import com.lojister.repository.abroudRepository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public File saveFile(File file) {
        return fileRepository.save(file);
    }


    public List<FileDto> listFile(String role, int advert_ID){
        List<File> listFile = fileRepository.listFile(role, advert_ID);
        List<FileDto> fileDtos = new ArrayList<>();

        for (File listFile2 : listFile) {
            FileDto fileDto2 = new FileDto();

            fileDto2.setId(listFile2.getId());
            fileDto2.setFileName(listFile2.getFileName());
            fileDto2.setName(listFile2.getName());
            fileDto2.setFiletype(listFile2.getContentType());

            fileDtos.add(fileDto2);
        }
        return fileDtos;
    }

    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Override
    public Optional<File> getFileById(Long id) {
        return fileRepository.findById(id);
    }
}
