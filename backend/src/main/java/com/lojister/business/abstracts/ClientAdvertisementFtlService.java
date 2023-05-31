package com.lojister.business.abstracts;

import com.lojister.controller.advertisement.SaveClientAdvertisementFtlRequest;

public interface ClientAdvertisementFtlService {
    void save(SaveClientAdvertisementFtlRequest dto);
    void checkedClientAdvertisementSaveDatesOperation(SaveClientAdvertisementFtlRequest saveClientAdvertisementFtlRequest);
    void checkedClientAdvertisementSaveTimesOperation(SaveClientAdvertisementFtlRequest saveClientAdvertisementFtlRequest);

}
