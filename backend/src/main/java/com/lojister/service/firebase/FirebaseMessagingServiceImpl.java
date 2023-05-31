package com.lojister.service.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;

import com.google.firebase.messaging.Notification;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.business.abstracts.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    private final DriverService driverService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Async
    public void sendNotificationToAllDriversForNewAdvertisements(ClientAdvertisement clientAdvertisement) {

        List<String> allTokenList = driverService.getAllFirebaseTokenFromDrivers();

        String bodyMessage = clientAdvertisement.getStartingAddress().getProvince()+", "+clientAdvertisement.getStartingAddress().getDistrict() +"  -->  "
                + clientAdvertisement.getDueAddress().getProvince()+", "+clientAdvertisement.getDueAddress().getDistrict() ;

        Notification notification = Notification
                .builder()
                .setTitle("Yeni Bir İlan Verildi !")
                .setBody(bodyMessage)
                .build();

        MulticastMessage message = MulticastMessage
                .builder()
                .putData("advertisementId",clientAdvertisement.getId().toString())
                .setNotification(notification)
                .addAllTokens(allTokenList)
                .build();

        FirebaseMessaging.getInstance().sendMulticastAsync(message);
    }
}
