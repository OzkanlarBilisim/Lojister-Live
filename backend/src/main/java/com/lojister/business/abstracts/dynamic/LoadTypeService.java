package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.LoadTypeDto;
import com.lojister.model.entity.adminpanel.LoadType;
import com.lojister.business.abstracts.BaseService;

import java.util.List;
import java.util.Set;

public interface LoadTypeService extends BaseService<LoadTypeDto> {

    void activate(Long id);

    void hide(Long id);

    List<LoadTypeDto> getActive();

    List<LoadTypeDto> getPassive();

    Set<LoadType> findSetLoadTypeByIdList(List<Long> idList);

    Set<LoadType> findSetLoadTypeByTypeNameList(List<String> typeNameList);

    void duplicateTypeNameCheck(String typeName);

    LoadType findDataById(Long id);

    Set<LoadType> findAll();
}
