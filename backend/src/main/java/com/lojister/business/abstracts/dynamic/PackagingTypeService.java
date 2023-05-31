package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.PackagingTypeDto;
import com.lojister.model.entity.adminpanel.PackagingType;
import com.lojister.business.abstracts.BaseService;

import java.util.List;

public interface PackagingTypeService extends BaseService<PackagingTypeDto> {

    void activate(Long id);

    void hide(Long id);

    List<PackagingTypeDto> getActive();

    List<PackagingTypeDto> getPassive();

    PackagingType findDataById(Long id);

    PackagingType findDataByTypeName(String typeName);

    void duplicateTypeNameCheck(String typeName);

}
