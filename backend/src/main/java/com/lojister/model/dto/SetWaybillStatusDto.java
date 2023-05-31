package com.lojister.model.dto;

import com.lojister.model.enums.WaybillStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SetWaybillStatusDto {

    @ApiModelProperty(value="ilgili kargo sürecinin id'si", required = true)
    @NotNull
    private Long transportProcessId;

    @ApiModelProperty(value="APPROVED ya da DENIED", required = true,allowableValues = "APPROVED,DENIED")
    @NotNull
    private WaybillStatus waybillStatus;

}
