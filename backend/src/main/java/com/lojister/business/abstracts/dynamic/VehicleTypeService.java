package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.VehicleTypeDto;
import com.lojister.model.entity.adminpanel.VehicleType;
import com.lojister.business.abstracts.BaseService;

import java.util.List;
import java.util.Set;

public interface VehicleTypeService extends BaseService<VehicleTypeDto> {

    void activate(Long id);

    void hide(Long id);

    List<VehicleTypeDto> getActive();

    List<VehicleTypeDto> getPassive();

    Set<VehicleType> findSetVehicleTypesByIdList(List<Long> idList);

    Set<VehicleType> findSetVehicleTypesByTypeNameList(List<String> typeNameList);

    void duplicateTypeNameCheck(String typeName);

    VehicleType findDataById(Long id);

    Set<VehicleType> findAll();

}
