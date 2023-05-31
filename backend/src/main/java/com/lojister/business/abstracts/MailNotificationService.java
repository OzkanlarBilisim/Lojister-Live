package com.lojister.business.abstracts;

import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.entity.client.ClientTransportProcess;

public interface MailNotificationService {

    void newClientAdvertisementSendMailToDriver(ClientAdvertisement clientAdvertisement);

    void clientAdvertisementBidSendMailToClient(ClientAdvertisementBid clientAdvertisementBid);

    void clientAdvertisementBidSendMailToDriver(ClientAdvertisementBid clientAdvertisementBid);

    void statusChangeClientAdvertisementSendMailToClient(ClientAdvertisement clientAdvertisement);
    void statusChangeClientAdvertisementBidSendMailToDriver(ClientAdvertisementBid clientAdvertisementBid);

    void statusChangeClientAdvertisementSendMailToDriver(ClientAdvertisement clientAdvertisement);
    void send(String toAddress, String subject, String content);
    void sendDriverAbroud(String subject, String content);
    void sendStatusChangeAbroud(String status, int advertId);
    void addBidSendMail(int adverId);
    void approvedMailSend(int bidID);

}
