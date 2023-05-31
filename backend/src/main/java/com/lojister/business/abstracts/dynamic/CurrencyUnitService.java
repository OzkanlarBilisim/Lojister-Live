package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.CurrencyUnitDto;
import com.lojister.model.entity.adminpanel.CurrencyUnit;
import com.lojister.business.abstracts.BaseServiceNoUpdate;

import java.util.List;

public interface CurrencyUnitService extends BaseServiceNoUpdate<CurrencyUnitDto> {

    void activate(Long id);

    void hide(Long id);

    List<CurrencyUnitDto> getActive();

    List<CurrencyUnitDto> getPassive();

    CurrencyUnit findDataById(Long id);

    CurrencyUnit findCurrencyUnitByCurrencyAbbreviation(String currencyAbbreviation);

    void duplicateCurrencyNameCheck(String currencyName);

    void duplicateCurrencyAbbreviationCheck(String currencyAbbreviation);

}
