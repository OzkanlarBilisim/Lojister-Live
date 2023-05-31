package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.enums.WaybillStatus;
import lombok.Data;

@Data
public class WaybillBase64Dto extends BaseDto {

    private String data;

    private String contentType;

    private WaybillStatus waybillStatus;

}
