package com.lojister.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClientCargoCompletedCheckDto {

    @NotNull
    private Boolean isSuccess;

    private String explanation;

}
