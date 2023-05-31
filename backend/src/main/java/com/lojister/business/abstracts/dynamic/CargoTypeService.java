package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.CargoTypeDto;
import com.lojister.model.entity.adminpanel.CargoType;
import com.lojister.business.abstracts.BaseService;

import java.util.List;
import java.util.Set;

public interface CargoTypeService extends BaseService<CargoTypeDto> {

    void activate(Long id);

    void hide(Long id);

    List<CargoTypeDto> getActive();

    List<CargoTypeDto> getPassive();

    Set<CargoType> findSetCargoTypesByIdList(List<Long> idList);

    Set<CargoType> findSetCargoTypesByNameList(List<String> typeNameList);

    void duplicateTypeNameCheck(String typeName);

    CargoType findDataById(Long id);

    Set<CargoType> findAll();
}
