package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.TrailerFloorTypeDto;
import com.lojister.model.entity.adminpanel.TrailerFloorType;
import com.lojister.business.abstracts.BaseService;

import java.util.List;
import java.util.Set;

public interface TrailerFloorTypeService extends BaseService<TrailerFloorTypeDto> {

    void activate(Long id);

    void hide(Long id);

    List<TrailerFloorTypeDto> getActive();

    List<TrailerFloorTypeDto> getPassive();

    Set<TrailerFloorType> findSetTrailerFloorTypesByIdList(List<Long> idList);

    Set<TrailerFloorType> findSetTrailerFloorTypesByTypeNameList(List<String> typeNameList);

    void duplicateTypeNameCheck(String typeName);

    TrailerFloorType findDataById(Long id);

    Set<TrailerFloorType> findAll();

}
