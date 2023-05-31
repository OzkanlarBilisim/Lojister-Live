package com.lojister.model.dto.dynamic;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.enums.DynamicStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyUnitDto extends BaseDto {

    @NotBlank
    @NotNull
    private String currencyName;

    @NotNull
    @NotBlank
    private String currencyAbbreviation;

    @NotBlank
    @NotNull
    private String currencySymbol;

    private DynamicStatus dynamicStatus;

}
