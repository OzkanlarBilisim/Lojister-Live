package com.lojister.business.abstracts;

import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientTransportProcess;

public interface SendMailService {

    void sendMailToAdminForNewAdvertisement(ClientAdvertisement clientAdvertisement);

    void sendMailAfterPaymentToInsuranceCompany(ClientTransportProcess clientTransportProcess);

}
