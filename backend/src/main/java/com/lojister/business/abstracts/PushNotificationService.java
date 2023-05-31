package com.lojister.business.abstracts;

import com.google.firebase.messaging.Notification;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;

import java.util.HashMap;
import java.util.List;

public interface PushNotificationService {

    void newClientAdvertisementSendMobileToDriver(ClientAdvertisement clientAdvertisement);

    void clientAdvertisementBidSendMobileToClient(ClientAdvertisementBid clientAdvertisementBid);

    void clientAdvertisementBidSendMobileToDriver(ClientAdvertisementBid clientAdvertisementBid);

    void statusChangeClientAdvertisementSendMobileToClient(ClientAdvertisement clientAdvertisement);
    void statusChangeClientAdvertisementBidSendMobileToDriver(ClientAdvertisementBid clientAdvertisementBid);
    void statusChangeClientAdvertisementSendMobileToDriver(ClientAdvertisement clientAdvertisement);
    void send(String idKey,String idValue, Notification notification, String token);
}
