package com.lojister.business.abstracts;

import com.lojister.model.entity.TransportProcess;

public interface TransportProcessService {

    TransportProcess findDataById(Long transportProcessId);

    TransportProcess findDataByTransportCode(String transportCode);

    TransportProcess saveRepo(TransportProcess transportProcess);

    void transportCodeStartsWithCheck(String transportCode);

    void cargoWasDeliveredCheck(TransportProcess transportProcess);

    void cargoOnTheWayCheck(TransportProcess transportProcess, String errorMessage,String errorMessageEn);

    void assignVehicleCheck(TransportProcess transportProcess);

    void paymentSuccessfulCheck(TransportProcess transportProcess);

}
