package com.lojister.controller.abroudControler;

import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.DocumentsAbroud;
import com.lojister.model.abroudModel.File;
import com.lojister.model.dto.abroudDto.FileDto;
import com.lojister.repository.abroudRepository.AbroudDocumentsRepository;
import com.lojister.repository.abroudRepository.FileRepository;
import com.lojister.service.abroudService.AbroudDocumentsService;
import com.lojister.service.abroudService.FileService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private  AbroudDocumentsRepository abroudDocumentsRepository;

    @Autowired
    private AbroudDocumentsService abroudDocumentsService;

    @Autowired
    private FileRepository fileRepository;

    @PostMapping("/fileDelete/{name}")
    public String deleteFile(@PathVariable("name") String name){
        File check = fileRepository.getFileName(name).orElse(null);

        if(check != null){
            check.setView(0);
            fileService.save(check);
        }
        return "Belge Silindi";
    }

    @GetMapping("/fileList/Client/{advert_ID}")
    public List<FileDto> listFileClient(@PathVariable("advert_ID") int advert_ID){
        return fileService.listFile("ROLE_CLIENT", advert_ID);
    }
    @GetMapping("/fileList/Driver/{advert_ID}")
    public List<FileDto> listFileDriver(@PathVariable("advert_ID") int advert_ID){
        return fileService.listFile("ROLE_DRIVER", advert_ID);
    }

    @PostMapping()
    public File uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("role") String role, @RequestParam("fileName") String fileName, @RequestParam("advert_ID") AdAbroud advert_ID) {
        String[] Permitted_File = {"image/jpeg","image/webp", "image/png", "application/pdf"};
        boolean Permitted_File_True = Arrays.asList(Permitted_File).contains(file.getContentType());

        if(Permitted_File_True){
            File check = fileRepository.getFileName(name).orElse(null);
            String rolex;

            if (role.equals("ROLE_CLIENT")) {
                rolex = "DRIVER";
            } else {
                rolex = "CLIENT";
            }
            Optional<DocumentsAbroud> listDocument = abroudDocumentsRepository.findWantingAndTransportID(rolex, advert_ID.getId());

            String[] documents = listDocument.get().getDocuments().split(",");
            boolean isDocumentValue = Arrays.asList(documents).contains(fileName);
            if (isDocumentValue) {

                String deger2 = null;
                int i_First = 0;
                for (int i = 0; i < documents.length; i++) {

                    if (i == i_First) {
                        if (documents[i].equals(fileName)) {
                            i_First++;
                        } else {
                            deger2 = documents[i];
                        }
                    } else {
                        if (!documents[i].equals(fileName)) {
                            deger2 = deger2 + "," + documents[i];
                        }
                    }
                }
                listDocument.get().setDocuments(deger2);
                abroudDocumentsService.saveStudent(listDocument.get());


                if (check != null) {
                    check.setView(0);
                    fileService.save(check);
                }
                File uploadedFile = new File();
                uploadedFile.setName(name);
                uploadedFile.setFileName(fileName);
                uploadedFile.setRole(role);
                uploadedFile.setAdAbroud(advert_ID);
                uploadedFile.setContentType(file.getContentType());
                uploadedFile.setView(1);
                try {
                    uploadedFile.setData(file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fileService.saveFile(uploadedFile);
            }
        }


        return null;
    }
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Optional<File> fileOptional = fileService.getFileById(id);
        if (!fileOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        File file = fileOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getContentType()));
        headers.setContentLength(file.getData().length);
        headers.setContentDispositionFormData("attachment", file.getFileName());
        return new ResponseEntity<>(file.getData(), headers, HttpStatus.OK);
    }
}

