package com.lojister.business.concretes;

import com.lojister.business.abstracts.DriverService;
import com.lojister.business.abstracts.UserService;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.entity.Company;
import com.lojister.model.entity.User;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.business.abstracts.MailNotificationService;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.enums.Language;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.repository.client.ClientRepository;
import com.lojister.repository.company.CompanyRepository;
import com.lojister.repository.driver.DriverRepository;
import com.lojister.service.abroudService.AbroudBidServiceImpl;
import lombok.*;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MailNotificationServiceImpl implements MailNotificationService {

    private final JavaMailSender mailSender;
    private final SecurityContextUtil securityContextUtil;
    private final static String SENDER_NAME = "Lojister";
    @Value("${lojister.mail.noReply.fromAddress}")
    private String fromAddress;

    @Value("${lojister.site.url}")
    private String FRONTEND_BASE_URL;
    private final DriverService driverService;
    private final TemplateEngine templateEngine;

    @Autowired
    private UserService userService;

    @Autowired
    private AbroudBidRepository abroudBidRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AbroudRepository abroudRepository;

    @Getter
    @Setter
    @AllArgsConstructor
    private class Attribute {
        String title;
        String value;
    }


    @Override
    @Async
    public void newClientAdvertisementSendMailToDriver(ClientAdvertisement clientAdvertisement) {
        List<Driver> driverList = driverService.findByDriverTitleIn(Arrays.asList(DriverTitle.BOSS));

        clientAdvertisement.nullCheckAndSetEmptyString();

        driverList.stream().forEach((driver) -> {

            if (driver.getNotificationSetting().getNewAdvertisementMailSending()) {

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                        .ofLocalizedDateTime(FormatStyle.MEDIUM)
                        .withLocale(Language.toLocale(driver.getLanguage()));
                List<Attribute> attributeList = new ArrayList<>();
                String subject = "";
                String message = "";
                String newNotification = "";
                String adInformation = "";
                Context ctx = new Context();
                String htmlContent;
                newNotification = Translator.toLocale("lojister.common.mail.newNotification", Language.toLocale(driver.getLanguage()));
                adInformation = Translator.toLocale("lojister.common.mail.adInformation", Language.toLocale(driver.getLanguage()));
                subject = Translator.toLocale("lojister.newAdvertisement.driver.subject", Language.toLocale(driver.getLanguage()));
                message = Translator.toLocale("lojister.newAdvertisement.driver.message", Language.toLocale(driver.getLanguage()));
                attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(driver.getLanguage())),
                        clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(driver.getLanguage())),
                        clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(driver.getLanguage())),
                        clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(driver.getLanguage())),
                        dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                ctx.setVariable("attributeList", attributeList);
                ctx.setVariable("message", message);
                ctx.setVariable("adInformation", adInformation);
                ctx.setVariable("newNotification", newNotification);
                htmlContent = this.templateEngine.process("notification_email", ctx);
                send(driver.getEmail(), subject, htmlContent);

            }
        });


    }

    @Override
    public void clientAdvertisementBidSendMailToClient(@NonNull ClientAdvertisementBid clientAdvertisementBid) {

        Client client = clientAdvertisementBid.getClientAdvertisement().getClient();
        ClientAdvertisement clientAdvertisement = clientAdvertisementBid.getClientAdvertisement();
        String message = "";
        String htmlContent = "";
        String subject = "";
        String newNotification = "";
        String adInformation = "";
        clientAdvertisement.nullCheckAndSetEmptyString();


        final Context ctx = new Context();
        List<Attribute> attributeList = new ArrayList<>();

        if (client.getNotificationSetting().getNewAdvertisementBidEmailSending()) {
            newNotification = Translator.toLocale("lojister.common.mail.newNotification", Language.toLocale(client.getLanguage()));
            adInformation = Translator.toLocale("lojister.common.mail.adInformation", Language.toLocale(client.getLanguage()));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.MEDIUM)
                    .withLocale(Language.toLocale(client.getLanguage()));
            subject = Translator.toLocale("lojister.newAdvertisementBid.client.subject", Language.toLocale(client.getLanguage()));
            message = Translator.toLocale("lojister.newAdvertisementBid.client.message", Language.toLocale(client.getLanguage()));

            attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisementBid.bidder", Language.toLocale(client.getLanguage())),
                    clientAdvertisementBid.getDriverBidder().getFirstName() + " " + clientAdvertisementBid.getDriverBidder().getLastName()));

            attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisementBid.bidPrice", Language.toLocale(client.getLanguage())),
                    clientAdvertisementBid.getBid().toString()));

            attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(client.getLanguage())),
                    clientAdvertisement.getClientAdvertisementCode()));

            attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(client.getLanguage())),
                    clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

            attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(client.getLanguage())),
                    clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

            attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(client.getLanguage())),
                    clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

            attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(client.getLanguage())),
                    dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

            ctx.setVariable("attributeList", attributeList);
            ctx.setVariable("message", message);
            ctx.setVariable("adInformation", adInformation);
            ctx.setVariable("newNotification", newNotification);
            htmlContent = this.templateEngine.process("notification_email", ctx);

            send(client.getEmail(), subject, htmlContent);
        }
    }

    @Override
    @Async
    public void clientAdvertisementBidSendMailToDriver(ClientAdvertisementBid clientAdvertisementBid) {
        Driver driver = clientAdvertisementBid.getDriverBidder();
        String message = "";
        String htmlContent = "";
        String newNotification = "";
        String adInformation = "";
        clientAdvertisementBid.getClientAdvertisement().nullCheckAndSetEmptyString();
        final Context ctx = new Context();
        List<Attribute> attributeList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Language.toLocale(driver.getLanguage()));
        newNotification = Translator.toLocale("lojister.common.mail.newNotification", Language.toLocale(driver.getLanguage()));
        adInformation = Translator.toLocale("lojister.common.mail.adInformation", Language.toLocale(driver.getLanguage()));
        String subject = Translator.toLocale("lojister.acceptBid.client.subject", Language.toLocale(driver.getLanguage()));
        message = Translator.toLocale("lojister.acceptBid.client.message", Language.toLocale(driver.getLanguage()));

        attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(driver.getLanguage())),
                clientAdvertisementBid.getClientAdvertisement().getClientAdvertisementCode()));

        attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(driver.getLanguage())),
                clientAdvertisementBid.getClientAdvertisement().getClient().getFirstName() + " " + clientAdvertisementBid.getClientAdvertisement().getClient().getLastName()));

        attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(driver.getLanguage())),
                clientAdvertisementBid.getClientAdvertisement().getStartingAddress().getProvince() + "  " + clientAdvertisementBid.getClientAdvertisement().getStartingAddress().getDistrict() + "  " + clientAdvertisementBid.getClientAdvertisement().getStartingAddress().getNeighborhood()));

        attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(driver.getLanguage())),
                clientAdvertisementBid.getClientAdvertisement().getDueAddress().getProvince() + "  " + clientAdvertisementBid.getClientAdvertisement().getDueAddress().getDistrict() + "  " + clientAdvertisementBid.getClientAdvertisement().getDueAddress().getNeighborhood()));

        attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(driver.getLanguage())),
                dateTimeFormatter.format(clientAdvertisementBid.getClientAdvertisement().getCreatedDateTime())));

        ctx.setVariable("attributeList", attributeList);
        ctx.setVariable("message", message);
        ctx.setVariable("adInformation", adInformation);
        ctx.setVariable("newNotification", newNotification);
        htmlContent = this.templateEngine.process("notification_email", ctx);
        send(driver.getEmail(), subject, htmlContent);
    }

    @Override
    @Async
    public void statusChangeClientAdvertisementSendMailToClient(ClientAdvertisement clientAdvertisement) {
        Client client = clientAdvertisement.getClient();
        clientAdvertisement.nullCheckAndSetEmptyString();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Language.toLocale(client.getLanguage()));
        String subject = "";
        String message = "";
        String htmlContent = "";
        String newNotification = "";
        String adInformation = "";
        final Context ctx = new Context();

        newNotification = Translator.toLocale("lojister.common.mail.newNotification", Language.toLocale(client.getLanguage()));
        adInformation = Translator.toLocale("lojister.common.mail.adInformation", Language.toLocale(client.getLanguage()));
        List<Attribute> attributeList = new ArrayList<>();

        if (client.getNotificationSetting().getStatusChangeAdvertisementEmailSending()) {
            switch (clientAdvertisement.getAdvertisementProcessStatus()) {
                case WAITING:

                    subject = Translator.toLocale("lojister.advertismentLive.client.subject", Language.toLocale(client.getLanguage()));
                    message = Translator.toLocale("lojister.advertismentLive.client.message", Language.toLocale(client.getLanguage()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(client.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(client.getEmail(), subject, htmlContent);
                    break;
                case BID_GIVEN:


                    break;
                case BID_APPROVED:
                    subject = Translator.toLocale("lojister.acceptAdvertisementBid.client.subject", Language.toLocale(client.getLanguage()));
                    message = Translator.toLocale("lojister.acceptAdvertisementBid.client.message", Language.toLocale(client.getLanguage()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(client.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(client.getEmail(), subject, htmlContent);
                    break;
                case PAYMENT_SUCCESSFUL:
                    subject = Translator.toLocale("lojister.paymentCompleted.client.subject", Language.toLocale(client.getLanguage()));
                    message = Translator.toLocale("lojister.paymentCompleted.client.message", Language.toLocale(client.getLanguage()));


                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(client.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(client.getEmail(), subject, htmlContent);
                    break;
                case ASSIGNED_VEHICLE:

                    subject = Translator.toLocale("lojister.assignedVehicle.client.subject", Language.toLocale(client.getLanguage()));
                    message = Translator.toLocale("lojister.assignedVehicle.client.message", Language.toLocale(client.getLanguage()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(client.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(client.getEmail(), subject, htmlContent);
                    break;
                case STARTING_TRANSPORT:

                    break;
                case CARGO_ON_THE_WAY:
                    subject = Translator.toLocale("lojister.cargoTransport.client.subject", Language.toLocale(client.getLanguage()));
                    message = Translator.toLocale("lojister.cargoTransport.client.message", Language.toLocale(client.getLanguage()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(client.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(client.getEmail(), subject, htmlContent);
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
                    subject = Translator.toLocale("lojister.transportCompleted.client.subject", Language.toLocale(client.getLanguage()));
                    message = Translator.toLocale("lojister.transportCompleted.client.message", Language.toLocale(client.getLanguage()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(client.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(client.getEmail(), subject, htmlContent);
                    break;
                case ENDING_TRANSPORT:
                    break;
                case HIDDEN:
                    subject = Translator.toLocale("lojister.advertismentHidden.client.subject", Language.toLocale(client.getLanguage()));
                    message = Translator.toLocale("lojister.advertismentHidden.client.message", Language.toLocale(client.getLanguage()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(client.getLanguage())),
                            clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(client.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(client.getEmail(), subject, htmlContent);
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    @Async
    public void statusChangeClientAdvertisementBidSendMailToDriver(ClientAdvertisementBid clientAdvertisementBid) {
        Driver driver = clientAdvertisementBid.getDriverBidder();
        clientAdvertisementBid.getClientAdvertisement().nullCheckAndSetEmptyString();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Language.toLocale(driver.getLanguage()));
        String subject = "";
        String message = "";
        String htmlContent = "";
        String newNotification = "";
        String adInformation = "";
        final Context ctx = new Context();
        newNotification = Translator.toLocale("lojister.common.mail.newNotification", Language.toLocale(driver.getLanguage()));
        adInformation = Translator.toLocale("lojister.common.mail.adInformation", Language.toLocale(driver.getLanguage()));

        List<Attribute> attributeList = new ArrayList<>();

        if (driver.getNotificationSetting().getStatusChangeAdvertisementEmailSending()) {

            switch (clientAdvertisementBid.getBidStatus()) {
                case CANCELLED:
                    break;
                case WAITING:
                    break;
                case DENIED:
                    subject = Translator.toLocale("lojister.advertisementBidDenied.client.subject", Language.toLocale(driver.getLanguage()));
                    message = Translator.toLocale("lojister.advertisementBidDenied.client.message", Language.toLocale(driver.getLanguage()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(driver.getLanguage())),
                            clientAdvertisementBid.getClientAdvertisement().getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(driver.getLanguage())),
                            clientAdvertisementBid.getClientAdvertisement().getClient().getFirstName() + " " + clientAdvertisementBid.getClientAdvertisement().getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(driver.getLanguage())),
                            clientAdvertisementBid.getClientAdvertisement().getStartingAddress().getProvince() + "  " + clientAdvertisementBid.getClientAdvertisement().getStartingAddress().getDistrict() + "  " + clientAdvertisementBid.getClientAdvertisement().getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(driver.getLanguage())),
                            clientAdvertisementBid.getClientAdvertisement().getDueAddress().getProvince() + "  " + clientAdvertisementBid.getClientAdvertisement().getDueAddress().getDistrict() + "  " + clientAdvertisementBid.getClientAdvertisement().getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(driver.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisementBid.getClientAdvertisement().getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(driver.getEmail(), subject, htmlContent);
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
    }

    @Override
    @Async
    public void statusChangeClientAdvertisementSendMailToDriver(ClientAdvertisement clientAdvertisement) {
        Driver driver = clientAdvertisement.getClientTransportProcess().getAcceptedClientAdvertisementBid().getDriverBidder();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Language.toLocale(driver.getLanguage()));
        String subject = "";
        String message = "";
        String htmlContent = "";
        String newNotification = "";
        String adInformation = "";
        final Context ctx = new Context();
        newNotification = Translator.toLocale("lojister.common.mail.newNotification", Language.toLocale(driver.getLanguage()));
        adInformation = Translator.toLocale("lojister.common.mail.adInformation", Language.toLocale(driver.getLanguage()));
        List<Attribute> attributeList = new ArrayList<>();

        if (driver.getNotificationSetting().getStatusChangeAdvertisementEmailSending()) {
            switch (clientAdvertisement.getAdvertisementProcessStatus()) {
                case WAITING:

                    break;
                case BID_GIVEN:

                    break;
                case BID_APPROVED:
                    subject = Translator.toLocale("lojister.acceptAdvertisementBid.driver.subject", Language.toLocale(driver.getLanguage()));
                    message = Translator.toLocale("lojister.acceptAdvertisementBid.driver.message", Language.toLocale(driver.getLanguage()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(driver.getLanguage())),
                            clientAdvertisement.getClientAdvertisementCode()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(driver.getLanguage())),
                            clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(driver.getLanguage())),
                            clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(driver.getLanguage())),
                            clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));

                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(driver.getLanguage())),
                            dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));

                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(driver.getEmail(), subject, htmlContent);
                    break;
                case PAYMENT_SUCCESSFUL:

                    subject = Translator.toLocale("lojister.paymentCompleted.driver.subject", Language.toLocale(driver.getLanguage()));
                    message = Translator.toLocale("lojister.paymentCompleted.driver.message", Language.toLocale(driver.getLanguage()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(driver.getLanguage())), clientAdvertisement.getClientAdvertisementCode()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(driver.getLanguage())), clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(driver.getLanguage())), clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(driver.getLanguage())), clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(driver.getLanguage())), dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));
                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(driver.getEmail(), subject, htmlContent);
                    break;
                case ASSIGNED_VEHICLE:

                    subject = Translator.toLocale("lojister.assignedVehicle.driver.subject", Language.toLocale(driver.getLanguage()));
                    message = Translator.toLocale("lojister.assignedVehicle.driver.message", Language.toLocale(driver.getLanguage()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(driver.getLanguage())), clientAdvertisement.getClientAdvertisementCode()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(driver.getLanguage())), clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(driver.getLanguage())), clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(driver.getLanguage())), clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(driver.getLanguage())), dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));
                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(driver.getEmail(), subject, htmlContent);
                    break;
                case STARTING_TRANSPORT:

                    break;
                case CARGO_ON_THE_WAY:
                    subject = Translator.toLocale("lojister.cargoTransport.driver.subject", Language.toLocale(driver.getLanguage()));
                    message = Translator.toLocale("lojister.cargoTransport.driver.message", Language.toLocale(driver.getLanguage()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(driver.getLanguage())), clientAdvertisement.getClientAdvertisementCode()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(driver.getLanguage())), clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(driver.getLanguage())), clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(driver.getLanguage())), clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(driver.getLanguage())), dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));
                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(driver.getEmail(), subject, htmlContent);
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
                    subject = Translator.toLocale("lojister.transportCompleted.driver.subject", Language.toLocale(driver.getLanguage()));
                    message = Translator.toLocale("lojister.transportCompleted.driver.message", Language.toLocale(driver.getLanguage()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertisementCode", Language.toLocale(driver.getLanguage())), clientAdvertisement.getClientAdvertisementCode()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.advertiser", Language.toLocale(driver.getLanguage())), clientAdvertisement.getClient().getFirstName() + " " + clientAdvertisement.getClient().getLastName()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.startingAddress", Language.toLocale(driver.getLanguage())), clientAdvertisement.getStartingAddress().getProvince() + "  " + clientAdvertisement.getStartingAddress().getDistrict() + "  " + clientAdvertisement.getStartingAddress().getNeighborhood()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.endingAddress", Language.toLocale(driver.getLanguage())), clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));
                    attributeList.add(new Attribute(Translator.toLocale("lojister.common.advertisement.createdDate", Language.toLocale(driver.getLanguage())), dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));
                    ctx.setVariable("attributeList", attributeList);
                    ctx.setVariable("message", message);
                    ctx.setVariable("adInformation", adInformation);
                    ctx.setVariable("newNotification", newNotification);
                    htmlContent = this.templateEngine.process("notification_email", ctx);
                    send(driver.getEmail(), subject, htmlContent);
                    break;
                case ENDING_TRANSPORT:
                    break;
                case HIDDEN:
                  /*  subject = "İlan Gizlendi!";
                    message="Aşağıda bilgileri bulunan ilan gizlenmiştir!";
                    attributeList.add(new Attribute("İlan Numarası",clientAdvertisement.getClientAdvertisementCode()));
                    attributeList.add(new Attribute("İlan Sahibinin Adı Soyadı",clientAdvertisement.getClient().getFirstName()));
                    attributeList.add(new Attribute("Başlangıç Adresi",clientAdvertisement.getStartingAddress().getProvince()+", "+clientAdvertisement.getStartingAddress().getDistrict()+", "+clientAdvertisement.getStartingAddress().getNeighborhood()));
                    attributeList.add(new Attribute("Varış Adresi",clientAdvertisement.getDueAddress().getProvince() + "  " + clientAdvertisement.getDueAddress().getDistrict() + "  " + clientAdvertisement.getDueAddress().getNeighborhood()));
                    attributeList.add(new Attribute("İlanın Açılma Tarihi",dateTimeFormatter.format(clientAdvertisement.getCreatedDateTime())));
                    ctx.setVariable("attributeList",attributeList);
                    ctx.setVariable("message",message);
                    htmlContent=this.templateEngine.process("notification_email", ctx);
                    send(client.getEmail(), subject, htmlContent);*/
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Async
    public void send(String toAddress, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {

            helper.setFrom(fromAddress, SENDER_NAME);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(message);

    }

    @Override
    public void sendDriverAbroud(String subject, String content) {
        List<Driver> driverList = driverService.findByDriverTitleIn(Arrays.asList(DriverTitle.BOSS));
        driverList.stream().forEach((driver) -> {
            if(driver.getNotificationSetting().getNewAdvertisementMailSending()){
                String toAddress = driver.getEmail();
                send(toAddress, subject, content);
            }
        });
    }
/*
    @Override
    public void sendDriverAbroud(String subject, String content) {
        List<User> users = userService.userDrivers();

        for (User user : users){

            Driver driverList = driverService.findDataById(user.getId());
            if(driverList.getNotificationSetting().getNewAdvertisementMailSending()){
                String toAddress = user.getEmail();
                send(toAddress, subject, content);
            }
        }
    }
*/

    @Override
    public void sendStatusChangeAbroud(String status, int advertId) {
        abroudBid abroudBid2 = abroudBidRepository.findbidPaymentSuccessAproved(advertId).get(0);

        User userClient = userService.findDataById(Long.valueOf(abroudBid2.getAdAbroud().getClient_id()));
        Client clientInfo = clientRepository.findById(userClient.getId()).get();

        User userDriver = userService.findDataById(abroudBid2.getUserDriver().getId());
        Driver driverInfo = driverRepository.findById(userDriver.getId()).get();

        String  toAddress    = null;
        String  subject      = null;
        String  url          = null;
        String  content      = null;
        Boolean permission   = false;

        switch (status){
            case "PAYMENT_SUCCESSFUL":
                subject = "Ödeme yapıldı";
                content = "<b>Yük sahibi ödemeyi yaptı, taşıma bilgilerini girmeniz gerekiyor.</b><br><br>";
                toAddress = userDriver.getEmail();
                url = "driver/offer-detail-abroud";
                permission = driverInfo.getNotificationSetting().getStatusChangeAdvertisementEmailSending();
                break;
            case "SHIPSMENTINFO":
                subject = "Yük taşıma bilgileri girildi";
                content = "<b>Yükün taşıyıcı tarafından teslim alınması bekleniyor.</b><br><br>";
                toAddress = userClient.getEmail();
                url = "client/transport-detail-abroud";
                permission = clientInfo.getNotificationSetting().getStatusChangeAdvertisementEmailSending();
                break;
            case "WAITING_FOR_TRANSPORT":
                toAddress = userClient.getEmail();
                content = "<b>Yükünüz teslim alındı, yükü teslim ettiğinizi onaylayın.</b><br><br>";
                subject = "Yük taşıma sürecinde";
                permission = clientInfo.getNotificationSetting().getStatusChangeAdvertisementEmailSending();
                url = "client/transport-detail-abroud";
                break;
            case "TRANSPORT":
                toAddress = userDriver.getEmail();
                content = "<b>Yük sahibi yükü teslim ettiğini onayladı, taşıma süreci başlamıştır.</b><br><br>";
                subject = "Yük taşıma sürecinde";
                url = "driver/offer-detail-abroud";
                permission = driverInfo.getNotificationSetting().getStatusChangeAdvertisementEmailSending();
                break;
            case "RATING":
                toAddress = userClient.getEmail();
                content = "<b>Ödemeniz Onaylandı.</b><br><br> <b>Lütfen Süreci değerlendirin.</b><br><br>";
                subject = "Süreci değerlendirin";
                url = "client/transport-detail-abroud";
                permission = clientInfo.getNotificationSetting().getStatusChangeAdvertisementEmailSending();
                break;
            case "TEMPORARY_METHOD":
                toAddress = userClient.getEmail();
                content = "<b>Yük taşıyıcı tarafından teslim edildi.</b><br><br> <b>Lütfen ödemenizi yapıp dokontu yükleyin.</b><br><br>";
                subject = "Dekont Yükleyin";
                url = "client/transport-detail-abroud";
                permission = clientInfo.getNotificationSetting().getStatusChangeAdvertisementEmailSending();
                break;
        }

        content = content + "<a href='"+FRONTEND_BASE_URL+"/"+url+"/"+abroudBid2.getAdAbroud().getId()+"'>Buraya</a>";
        content = content + " tıklayarak ilan detaylarına gidebilirsiniz.<br><br>";

        if(permission){
             send(toAddress, subject, content);
        }
    }
    @Override
    public void addBidSendMail(int advertId)      {
        AdAbroud abroud = abroudRepository.findById(advertId).get();

        User userClient = userService.findDataById(Long.valueOf(abroud.getClient_id()));
        Client clientInfo = clientRepository.findById(userClient.getId()).get();

        String toAddress = userClient.getEmail();
        String content = "<b>İlanınıza yeni bir teklif gelmiştir.</b><br><br>";
        String subject = "Yeni teklif";

        Boolean permission = clientInfo.getNotificationSetting().getStatusChangeAdvertisementEmailSending();

        content = content + "<a href='"+FRONTEND_BASE_URL+"/client/transport-detail-abroud/"+abroud.getId()+"'>Buraya</a>";
        content = content + " tıklayarak ilan detaylarına gidebilirsiniz.<br><br>";

        if(permission){
            send(toAddress, subject, content);
        }
    }

    @Override
    public void approvedMailSend(int bidID){
        abroudBid abroudBid2 = abroudBidRepository.findById(bidID).get();

        User userDriver = userService.findDataById(abroudBid2.getUserDriver().getId());
        Driver driverInfo = driverRepository.findById(userDriver.getId()).get();

        String toAddress = userDriver.getEmail();
        String content = "<b>Teklifiniz kabul edildi.</b><br><br>";
        String subject = "Teklifiniz kabul edildi.i";

        Boolean permission = driverInfo.getNotificationSetting().getStatusChangeAdvertisementEmailSending();

        content = content + "<a href='"+FRONTEND_BASE_URL+"/driver/offer-detail-abroud/"+abroudBid2.getAdAbroud().getId()+"'>Buraya</a>";
        content = content + " tıklayarak ilan detaylarına gidebilirsiniz.<br><br>";

        if(permission){
            send(toAddress, subject, content);
        }
    }
}
