package com.lojister.business.concretes;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.lojister.business.abstracts.DriverService;
import com.lojister.business.abstracts.PushNotificationService;
import com.lojister.core.i18n.Translator;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.enums.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class PushNotificationManager implements PushNotificationService {

    private final DriverService driverService;

    @Override
    @Async
    public void newClientAdvertisementSendMobileToDriver(ClientAdvertisement clientAdvertisement) {
        List<Driver> driverList = driverService.findByDriverTitleIn(Arrays.asList(DriverTitle.BOSS));
        driverList.stream().filter(driver -> driver.getNotificationSetting().getNewAdvertisementMobileSending()&&driver.getFirebaseToken()!=null)
                .forEach((driver) -> {
                    String bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                    Notification notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.newAdvertisement.driver.subject", Language.toLocale(driver.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, driver.getFirebaseToken());
               });


    }

    @Override
    @Async
    public void clientAdvertisementBidSendMobileToClient(ClientAdvertisementBid clientAdvertisementBid) {
        Client client = clientAdvertisementBid.getClientAdvertisement().getClient();
        ClientAdvertisement clientAdvertisement = clientAdvertisementBid.getClientAdvertisement();
        if (client.getNotificationSetting().getNewAdvertisementBidEmailSending()) {
            String bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                    + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
            Notification notification = Notification
                    .builder()
                    .setTitle(Translator.toLocale("lojister.newAdvertisementBid.client.subject", Language.toLocale(client.getLanguage())))
                    .setBody(bodyMessage)
                    .build();
            send("advertisementBidId", clientAdvertisementBid.getBid().toString(), notification, client.getFirebaseToken());
        }
    }

    @Override
    @Async
    public void clientAdvertisementBidSendMobileToDriver(ClientAdvertisementBid clientAdvertisementBid) {
        Client client = clientAdvertisementBid.getClientAdvertisement().getClient();
        ClientAdvertisement clientAdvertisement = clientAdvertisementBid.getClientAdvertisement();
        if (client.getNotificationSetting().getNewAdvertisementBidEmailSending()) {
            String bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                    + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
            Notification notification = Notification
                    .builder()
                    .setTitle(Translator.toLocale("lojister.acceptAdvertisementBid.driver.subject", Language.toLocale(client.getLanguage())))
                    .setBody(bodyMessage)
                    .build();
            send("advertisementBidId", clientAdvertisementBid.getBid().toString(), notification, client.getFirebaseToken());
        }

    }

    @Override
    @Async
    public void statusChangeClientAdvertisementSendMobileToClient(ClientAdvertisement clientAdvertisement) {
        Client client = clientAdvertisement.getClient();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.FULL)
                .withZone(ZoneId.systemDefault());
        String bodyMessage="";
        Notification notification;
        if (client.getNotificationSetting().getStatusChangeAdvertisementEmailSending()) {
            switch (clientAdvertisement.getAdvertisementProcessStatus()) {
                case WAITING:
                     bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                     notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.advertismentLive.client.subject", Language.toLocale(client.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, client.getFirebaseToken());
                    break;
                case BID_GIVEN:


                    break;
                case BID_APPROVED:
                     bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                     notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.acceptAdvertisementBid.client.subject", Language.toLocale(client.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, client.getFirebaseToken());
                    break;
                case PAYMENT_SUCCESSFUL:
                     bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                     notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.paymentCompleted.client.subject", Language.toLocale(client.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, client.getFirebaseToken());
                    break;
                case ASSIGNED_VEHICLE:
                     bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                     notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.assignedVehicle.client.subject", Language.toLocale(client.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, client.getFirebaseToken());
                    break;
                case STARTING_TRANSPORT:

                    break;
                case CARGO_ON_THE_WAY:
                     bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                     notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.cargoTransport.client.subject", Language.toLocale(client.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, client.getFirebaseToken());
                    break;
                case WAITING_WAYBILL:
                    break;
                case WAYBILL_DENIED:
                    break;
                case CARGO_COULD_NOT_BE_DELIVERED:
                    break;
                case CLIENT_CARGO_PROBLEM:
                    break;
                case COMPLETED:
                     bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                     notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.transportCompleted.client.subject", Language.toLocale(client.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, client.getFirebaseToken());
                    break;
                case ENDING_TRANSPORT:
                    break;
                case HIDDEN:
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    @Async
    public void statusChangeClientAdvertisementBidSendMobileToDriver(ClientAdvertisementBid clientAdvertisementBid) {
        Driver driver= clientAdvertisementBid.getDriverBidder();
        switch (clientAdvertisementBid.getBidStatus()) {
            case CANCELLED:
                break;
            case WAITING:
                break;
            case DENIED:

                break;
            case TIMEOUT:
                break;
            case APPROVED:
                break;
            case TRANSPORT:
                break;
            case AD_CLOSED:
                break;
            case COMPLETED:
                break;
            default:
                break;
        }
    }

    @Override
    @Async
    public void statusChangeClientAdvertisementSendMobileToDriver(ClientAdvertisement clientAdvertisement) {
        Driver driver = clientAdvertisement.getClientTransportProcess().getAcceptedClientAdvertisementBid().getDriverBidder();

        String bodyMessage="";
        Notification notification;
        if (driver.getNotificationSetting().getStatusChangeAdvertisementEmailSending()) {
            switch (clientAdvertisement.getAdvertisementProcessStatus()) {
                case WAITING:
          /*          bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                    notification = Notification
                            .builder()
                            .setTitle("Yeni Bir İlan Verildi!")
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, driver.getFirebaseToken());*/
                    break;
                case BID_GIVEN:


                    break;
                case BID_APPROVED:
                    bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                    notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.acceptBid.client.subject", Language.toLocale(driver.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, driver.getFirebaseToken());
                    break;
                case PAYMENT_SUCCESSFUL:
                    bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                    notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.paymentCompleted.client.subject", Language.toLocale(driver.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, driver.getFirebaseToken());
                    break;
                case ASSIGNED_VEHICLE:
                    bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                    notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.assignedVehicle.client.subject", Language.toLocale(driver.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, driver.getFirebaseToken());
                    break;
                case STARTING_TRANSPORT:

                    break;
                case CARGO_ON_THE_WAY:
                    bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                    notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.cargoTransport.client.subject", Language.toLocale(driver.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, driver.getFirebaseToken());
                    break;
                case WAITING_WAYBILL:
                    break;
                case WAYBILL_DENIED:
                    break;
                case CARGO_COULD_NOT_BE_DELIVERED:
                    break;
                case CLIENT_CARGO_PROBLEM:
                    break;
                case COMPLETED:
                    bodyMessage = clientAdvertisement.getStartingAddress().getProvince() + ", " + clientAdvertisement.getStartingAddress().getDistrict() + "  -->  "
                            + clientAdvertisement.getDueAddress().getProvince() + ", " + clientAdvertisement.getDueAddress().getDistrict();
                    notification = Notification
                            .builder()
                            .setTitle(Translator.toLocale("lojister.transportCompleted.client.subject", Language.toLocale(driver.getLanguage())))
                            .setBody(bodyMessage)
                            .build();
                    send("advertisementId", clientAdvertisement.getId().toString(), notification, driver.getFirebaseToken());
                    break;
                case ENDING_TRANSPORT:
                    break;
                case HIDDEN:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    @Async
    @Transactional(Transactional.TxType.REQUIRES_NEW)

    public void send(String idKey, String idValue, Notification notification, String token) {
       if(token!=null){
           MulticastMessage message = MulticastMessage
                   .builder()
                   .putData(idKey, idValue)
                   .setNotification(notification)
                   .addToken(token)
                   .build();

           FirebaseMessaging.getInstance().sendMulticastAsync(message);
       }

    }


}
