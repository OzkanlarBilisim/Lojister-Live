package com.lojister.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SendMailConfirmationTransportDto {

    String token;
    String transportCode;
    String email;
}
