package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.VehicleDocumentTypeDto;
import com.lojister.model.entity.adminpanel.VehicleDocumentType;
import com.lojister.business.abstracts.BaseServiceNoUpdate;

import java.util.List;

public interface VehicleDocumentTypeService extends BaseServiceNoUpdate<VehicleDocumentTypeDto> {

    void activate(Long id);

    void hide(Long id);

    List<VehicleDocumentTypeDto> getActive();

    List<VehicleDocumentTypeDto> getPassive();

    void duplicateTypeNameCheck(String typeName);

    VehicleDocumentType findDataById(Long id);

}
