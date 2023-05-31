package com.lojister.business.abstracts;

import com.lojister.model.dto.BankInformationDto;
import com.lojister.model.entity.BankInformation;

public interface BankInformationService extends BaseService<BankInformationDto> {

    BankInformation saveRepo(BankInformation bankInformation);

    BankInformation findDataById(Long id);

}
