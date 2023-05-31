package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.base.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerificationTokenDto extends BaseDto {

    private String verificationToken;

    private LocalDateTime verificationCreatedDateTime;

    private UserDto user;
}
