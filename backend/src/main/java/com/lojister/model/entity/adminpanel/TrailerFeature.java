package com.lojister.model.entity.adminpanel;

import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrailerFeature extends BaseEntity {

    @Column(unique = true)
    private String featureName;

    @Column(unique = true)
    private String engFeatureName;

    @Enumerated(EnumType.STRING)
    private DynamicStatus dynamicStatus;

    public TrailerFeature(String featureName, DynamicStatus dynamicStatus) {
        this.featureName = featureName;
        this.dynamicStatus = dynamicStatus;
    }

    public static TrailerFeature create(String trFeatureName,String engFeatureName){
        TrailerFeature trailerFeature = new TrailerFeature();
        trailerFeature.featureName = trFeatureName;
        trailerFeature.engFeatureName = engFeatureName;
        trailerFeature.dynamicStatus = DynamicStatus.ACTIVE;
        return trailerFeature;
    }
}
