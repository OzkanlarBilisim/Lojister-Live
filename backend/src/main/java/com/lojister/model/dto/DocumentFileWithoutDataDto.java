package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.base.UserDto;
import com.lojister.model.entity.User;
import com.lojister.model.enums.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DocumentFileWithoutDataDto  {
    private Long id;
    private String fileName;
    private String contentType;
    private User user;
    DocumentType documentType;
}
