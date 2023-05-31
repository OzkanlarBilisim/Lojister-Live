package com.lojister.model.dto;

import com.lojister.model.dto.base.UserDto;
import lombok.Data;

import java.sql.Blob;

@Data
public class ProfilePhotoFileDto {

    private UserDto user;

    private String fileName;

    private String path;

    private Blob data;

}
