package com.lojister.service.firebase;

import com.lojister.model.entity.client.ClientAdvertisement;

public interface FirebaseMessagingService {

    void sendNotificationToAllDriversForNewAdvertisements(ClientAdvertisement clientAdvertisement);
}
