package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.TrailerFeatureDto;
import com.lojister.model.entity.adminpanel.TrailerFeature;
import com.lojister.business.abstracts.BaseService;

import java.util.List;
import java.util.Set;

public interface TrailerFeatureService extends BaseService<TrailerFeatureDto> {

    void activate(Long id);

    void hide(Long id);

    List<TrailerFeatureDto> getActive();

    List<TrailerFeatureDto> getPassive();

    Set<TrailerFeature> findSetTrailerFeaturesByIdList(List<Long> idList);

    Set<TrailerFeature> findSetTrailerFeaturesByFeatureNameList(List<String> featureNameList);

    void duplicateFeatureNameCheck(String featureName);

    TrailerFeature findDataById(Long id);

    Set<TrailerFeature> findAll();
}
