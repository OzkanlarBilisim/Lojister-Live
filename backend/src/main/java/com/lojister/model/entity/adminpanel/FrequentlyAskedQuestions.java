package com.lojister.model.entity.adminpanel;

import com.lojister.model.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Data
public class FrequentlyAskedQuestions extends BaseEntity {

    @Lob
    private String tr_question;

    @Lob
    private String tr_answer;

    @Lob
    private String eng_question;

    @Lob
    private String eng_answer;

}
