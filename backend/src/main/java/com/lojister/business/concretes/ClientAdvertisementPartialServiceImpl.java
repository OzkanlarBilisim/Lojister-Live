package com.lojister.business.concretes;

import com.lojister.business.abstracts.*;
import com.lojister.business.concretes.dynamic.TrailerTypeServiceImpl;
import com.lojister.business.abstracts.dynamic.*;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.ConfirmedClient;
import com.lojister.controller.advertisement.SaveClientAdvertisementPartialRequest;
import com.lojister.core.exception.InvalidDateTimeException;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.SavedAddress;
import com.lojister.model.entity.SimpleAdvertisementAddress;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisementPartial;
import com.lojister.repository.advertisement.ClientAdvertisementPartialRepository;
import com.lojister.service.firebase.FirebaseMessagingService;
import com.lojister.core.util.LocalDateTimeParseUtil;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.validator.formatter.PhoneFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientAdvertisementPartialServiceImpl implements ClientAdvertisementPartialService {

    private final ClientAdvertisementPartialRepository clientAdvertisementPartialRepository;
    private final SecurityContextUtil securityContextUtil;
    private final TrailerTypeServiceImpl trailerTypeService;
    private final TrailerFloorTypeService trailerFloorTypeService;
    private final TrailerFeatureService trailerFeatureService;
    private final PackagingTypeService packagingTypeService;
    private final CargoTypeService cargoTypeService;
    private final LoadTypeService loadTypeService;
    private final CurrencyUnitService currencyUnitService;
    private final LocalDateTimeParseUtil localDateTimeParseUtil;
    private final PhoneFormatter phoneFormatter;
    private final FirebaseMessagingService firebaseMessagingService;
    private final SendMailService sendMailService;
    private final AdvertisementLogic advertisementLogic;
    private final SavedAddressService savedAddressService;

    private  final MailNotificationService mailNotificationService;
    private final PushNotificationService pushNotificationService;
    @Override
    @ConfirmedClient
    public void save(SaveClientAdvertisementPartialRequest saveClientAdvertisementPartialRequest) {
        Client currentClient = securityContextUtil.getCurrentClient();

        //Date Check Function
        checkedClientAdvertisementSaveDatesOperation(saveClientAdvertisementPartialRequest);
        checkedClientAdvertisementSaveTimesOperation(saveClientAdvertisementPartialRequest);

        Set<TrailerType> trailerTypeSet = saveClientAdvertisementPartialRequest.getTrailerTypeIdList() == null || saveClientAdvertisementPartialRequest.getTrailerTypeIdList().isEmpty() ? trailerTypeService.findAll() : trailerTypeService.findSetTrailerTypesByIdList(saveClientAdvertisementPartialRequest.getTrailerTypeIdList());
        Set<TrailerFloorType> trailerFloorTypeSet = saveClientAdvertisementPartialRequest.getTrailerFloorTypeIdList() == null || saveClientAdvertisementPartialRequest.getTrailerFloorTypeIdList().isEmpty() ? trailerFloorTypeService.findAll() : trailerFloorTypeService.findSetTrailerFloorTypesByIdList(saveClientAdvertisementPartialRequest.getTrailerFloorTypeIdList());
        Set<TrailerFeature> trailerFeatureSet = saveClientAdvertisementPartialRequest.getTrailerFeatureIdList() == null || saveClientAdvertisementPartialRequest.getTrailerFeatureIdList().isEmpty() ? trailerFeatureService.findAll() :  trailerFeatureService.findSetTrailerFeaturesByIdList(saveClientAdvertisementPartialRequest.getTrailerFeatureIdList());

        Recipient startRecipient = saveClientAdvertisementPartialRequest.getStartRecipient();
        String formattedNumber = phoneFormatter.format(startRecipient.getPhoneNumber());
        startRecipient.setPhoneNumber(formattedNumber);

        Recipient dueRecipient = saveClientAdvertisementPartialRequest.getDueRecipient();
        String formattedNumber2 = phoneFormatter.format(dueRecipient.getPhoneNumber());
        dueRecipient.setPhoneNumber(formattedNumber2);

        PackagingType packagingType = packagingTypeService.findDataById(saveClientAdvertisementPartialRequest.getPackagingTypeId());
        Set<CargoType> cargoTypeSet = saveClientAdvertisementPartialRequest.getCargoTypeIdList()==null || saveClientAdvertisementPartialRequest.getCargoTypeIdList().isEmpty() ? cargoTypeService.findAll() : cargoTypeService.findSetCargoTypesByIdList(saveClientAdvertisementPartialRequest.getCargoTypeIdList());
        Set<LoadType> loadTypes = saveClientAdvertisementPartialRequest.getLoadTypeIdList() == null || saveClientAdvertisementPartialRequest.getLoadTypeIdList().isEmpty() ? loadTypeService.findAll() : loadTypeService.findSetLoadTypeByIdList(saveClientAdvertisementPartialRequest.getLoadTypeIdList());

        CurrencyUnit currencyUnit = currencyUnitService.findDataById(saveClientAdvertisementPartialRequest.getCurrencyUnitId());
        SimpleAdvertisementAddress simpleStartingAddress=advertisementLogic.createSimpleAdvertisementAddress(saveClientAdvertisementPartialRequest.getStartingAddress());
        SimpleAdvertisementAddress simpleDueAddress=advertisementLogic.createSimpleAdvertisementAddress(saveClientAdvertisementPartialRequest.getDueAddress());
        String clientAdvertisementCode=advertisementLogic.createAdvertisementCode();
        ClientAdvertisementPartial clientAdvertisementPartial=ClientAdvertisementPartial.save(
                saveClientAdvertisementPartialRequest
                ,currentClient
                ,trailerTypeSet
                ,trailerFloorTypeSet
                ,trailerFeatureSet
                ,startRecipient
                ,dueRecipient
                ,packagingType
                ,cargoTypeSet
                ,loadTypes
                ,currencyUnit
                ,simpleStartingAddress
                ,simpleDueAddress
                ,clientAdvertisementCode);
        clientAdvertisementPartialRepository.save(clientAdvertisementPartial);
        sendMailService.sendMailToAdminForNewAdvertisement(clientAdvertisementPartial);
        mailNotificationService.newClientAdvertisementSendMailToDriver(clientAdvertisementPartial);
        mailNotificationService.statusChangeClientAdvertisementSendMailToClient(clientAdvertisementPartial);
        pushNotificationService.newClientAdvertisementSendMobileToDriver(clientAdvertisementPartial);
        pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(clientAdvertisementPartial);

    }
    /**
     * İlanın Başlangıç veya Bitiş Tarihinin Şimdiki Zamandan Geride Olup Olmadığını Kontrol Eder.
     * İlanın Bitiş Tarihinin Başlangıç Tarihinden Önce Olup Olmadığını Kontrol Eder.
     *
     * @param saveClientAdvertisementPartialRequest İlan Kaydetme Dto Nesnesi.
     * @throws InvalidDateTimeException Zaman Hatalarını Döndürür.
     */
    @Override
    public void checkedClientAdvertisementSaveDatesOperation(SaveClientAdvertisementPartialRequest saveClientAdvertisementPartialRequest) {
        LocalDateTime startingLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementPartialRequest.getAdStartingDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementPartialRequest.getAdStartingTime()));
        LocalDateTime dueLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementPartialRequest.getAdDueDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementPartialRequest.getAdDueTime()));

        if (startingLocalDateTime.isBefore(LocalDateTime.now()) || dueLocalDateTime.isBefore(LocalDateTime.now())) {

            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementPartial.InvalidDateTimeException.notAvailable"));

        } else if (dueLocalDateTime.isBefore(startingLocalDateTime)) {
            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementPartial.InvalidDateTimeException.endTime"));
        }

    }
    /**
     * İlan Kaydetme Nesnesi İçindeki Başlangıç ve Bitiş Tarihi Arasında 120 Dakika(2 Saat) Olup Olmadığını Kontrol Eder.
     *
     * @param saveClientAdvertisementPartialRequest Client İlan Kaydetme Dto Nesnesi
     */
    @Override
    public void checkedClientAdvertisementSaveTimesOperation(SaveClientAdvertisementPartialRequest saveClientAdvertisementPartialRequest) {
        LocalDateTime startingLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementPartialRequest.getAdStartingDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementPartialRequest.getAdStartingTime()));
        LocalDateTime dueLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementPartialRequest.getAdDueDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementPartialRequest.getAdDueTime()));

        long minutes = ChronoUnit.MINUTES.between(startingLocalDateTime, dueLocalDateTime);

        if (minutes < 120) {
            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementPartial.InvalidDateTimeException.twoHour"));
        }
    }




}
