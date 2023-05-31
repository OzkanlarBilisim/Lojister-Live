package com.lojister.model.dto.dynamic;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.enums.DynamicStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PackagingTypeDto extends BaseDto {

    @NotNull
    @NotBlank
    private String typeName;

    private String engTypeName;

    private DynamicStatus dynamicStatus;
}
