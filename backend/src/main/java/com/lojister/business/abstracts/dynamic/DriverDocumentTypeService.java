package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.DriverDocumentTypeDto;
import com.lojister.model.entity.adminpanel.DriverDocumentType;
import com.lojister.business.abstracts.BaseServiceNoUpdate;

import java.util.List;

public interface DriverDocumentTypeService extends BaseServiceNoUpdate<DriverDocumentTypeDto> {

    void activate(Long id);

    void hide(Long id);

    List<DriverDocumentTypeDto> getActive();

    List<DriverDocumentTypeDto> getPassive();

    void duplicateTypeNameCheck(String typeName);

    DriverDocumentType findDataById(Long id);

}
