package com.lojister.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TransportCodeDto {

    @NotNull
    @NotBlank
    @Size(min = 17,max = 17)
    private String transportCode;
}
