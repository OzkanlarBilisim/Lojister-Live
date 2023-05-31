package com.lojister.model.dto.dynamic;

import com.lojister.model.dto.base.BaseDto;
import lombok.Data;

@Data
public class AboutUsDto extends BaseDto {

    private String tr_explanation;

    private String eng_explanation;
}
