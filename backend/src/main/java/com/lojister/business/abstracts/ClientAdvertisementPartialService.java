package com.lojister.business.abstracts;

import com.lojister.controller.advertisement.SaveClientAdvertisementPartialRequest;

public interface ClientAdvertisementPartialService {

    void save(SaveClientAdvertisementPartialRequest dto);

    void checkedClientAdvertisementSaveDatesOperation(SaveClientAdvertisementPartialRequest saveClientAdvertisementPartialRequest);

    void checkedClientAdvertisementSaveTimesOperation(SaveClientAdvertisementPartialRequest saveClientAdvertisementPartialRequest);

}
