package com.lojister.model.dto.dynamic;

import com.lojister.model.dto.base.BaseDto;
import lombok.Data;

@Data
public class FrequentlyAskedQuestionsDto extends BaseDto {

    private String tr_question;

    private String tr_answer;

    private String eng_question;

    private String eng_answer;

}
