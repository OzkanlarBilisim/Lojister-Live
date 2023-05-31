package com.lojister.model.entity.adminpanel;

import com.lojister.model.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Data
public class AboutUs extends BaseEntity {

    @Lob
    private String tr_explanation;

    @Lob
    private String eng_explanation;

}
