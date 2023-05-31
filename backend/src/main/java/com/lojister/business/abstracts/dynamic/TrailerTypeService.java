package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.TrailerTypeDto;
import com.lojister.model.entity.adminpanel.TrailerType;
import com.lojister.business.abstracts.BaseService;

import java.util.List;
import java.util.Set;

public interface TrailerTypeService extends BaseService<TrailerTypeDto> {

    void activate(Long id);

    void hide(Long id);

    List<TrailerTypeDto> getActive();

    List<TrailerTypeDto> getPassive();

    Set<TrailerType> findSetTrailerTypesByIdList(List<Long> idList);

    Set<TrailerType> findSetTrailerTypesByTypeNameList(List<String> typeNameList);

    void duplicateTypeNameCheck(String typeName);

    TrailerType findDataById(Long id);

   Set<TrailerType> findAll();




}
