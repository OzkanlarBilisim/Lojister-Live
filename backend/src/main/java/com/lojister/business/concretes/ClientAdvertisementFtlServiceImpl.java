package com.lojister.business.concretes;

import com.lojister.business.abstracts.*;
import com.lojister.business.concretes.dynamic.TrailerTypeServiceImpl;
import com.lojister.business.abstracts.dynamic.*;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.ConfirmedClient;
import com.lojister.controller.advertisement.SaveClientAdvertisementFtlRequest;
import com.lojister.core.exception.InvalidDateTimeException;
import com.lojister.model.dto.GeoCodeAddressDto;
import com.lojister.model.dto.PositionDto;
import com.lojister.model.entity.*;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisementFtl;
import com.lojister.repository.advertisement.ClientAdvertisementFtlRepository;
import com.lojister.service.firebase.FirebaseMessagingService;
import com.lojister.core.util.LocalDateTimeParseUtil;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.validator.formatter.PhoneFormatter;
import com.lojister.service.gmaps.GMapsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientAdvertisementFtlServiceImpl implements ClientAdvertisementFtlService {

    private final ClientAdvertisementFtlRepository clientAdvertisementFtlRepository;
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
    private final VehicleTypeService vehicleTypeService;
    private final AdvertisementLogic advertisementLogic;
    private final SavedAddressService savedAddressService;
    private  final MailNotificationService mailNotificationService;
    private final PushNotificationService pushNotificationService;
    private final GMapsService gMapsService;



    @Override
    @ConfirmedClient
    public void save(SaveClientAdvertisementFtlRequest saveClientAdvertisementFtlRequest) {
        Client currentClient = securityContextUtil.getCurrentClient();
        //Date Check Function
        checkedClientAdvertisementSaveDatesOperation(saveClientAdvertisementFtlRequest);
        checkedClientAdvertisementSaveTimesOperation(saveClientAdvertisementFtlRequest);











        GeoCodeAddressDto dueAddressDto = new GeoCodeAddressDto();

        dueAddressDto.setFullAddress(saveClientAdvertisementFtlRequest.getDueAddress().getFullAddress());
        dueAddressDto.setStreet(saveClientAdvertisementFtlRequest.getDueAddress().getStreet());
        dueAddressDto.setDistrict(saveClientAdvertisementFtlRequest.getDueAddress().getDistrict());
        dueAddressDto.setProvince(saveClientAdvertisementFtlRequest.getDueAddress().getProvince());
        dueAddressDto.setNeighborhood(saveClientAdvertisementFtlRequest.getDueAddress().getNeighborhood());

        PositionDto dueAddressLtdLng = gMapsService.geocodeFromAddress(dueAddressDto);

        AdvertisementAddress dueAddress = new AdvertisementAddress();

        dueAddress.setFullAddress(saveClientAdvertisementFtlRequest.getDueAddress().getFullAddress());
        dueAddress.setStreet(saveClientAdvertisementFtlRequest.getDueAddress().getStreet());
        dueAddress.setDistrict(saveClientAdvertisementFtlRequest.getDueAddress().getDistrict());
        dueAddress.setProvince(saveClientAdvertisementFtlRequest.getDueAddress().getProvince());
        dueAddress.setNeighborhood(saveClientAdvertisementFtlRequest.getDueAddress().getNeighborhood());
        dueAddress.setLat(dueAddressLtdLng.getLatitude());
        dueAddress.setLng(dueAddressLtdLng.getLongitude());

        saveClientAdvertisementFtlRequest.setDueAddress(dueAddress);






        GeoCodeAddressDto startingAddressDto = new GeoCodeAddressDto();

        startingAddressDto.setFullAddress(saveClientAdvertisementFtlRequest.getStartingAddress().getFullAddress());
        startingAddressDto.setStreet(saveClientAdvertisementFtlRequest.getStartingAddress().getStreet());
        startingAddressDto.setDistrict(saveClientAdvertisementFtlRequest.getStartingAddress().getDistrict());
        startingAddressDto.setProvince(saveClientAdvertisementFtlRequest.getStartingAddress().getProvince());
        startingAddressDto.setNeighborhood(saveClientAdvertisementFtlRequest.getStartingAddress().getNeighborhood());

        PositionDto startingAddressLtdLng = gMapsService.geocodeFromAddress(startingAddressDto);

        AdvertisementAddress startingAddress = new AdvertisementAddress();

        startingAddress.setFullAddress(saveClientAdvertisementFtlRequest.getStartingAddress().getFullAddress());
        startingAddress.setStreet(saveClientAdvertisementFtlRequest.getStartingAddress().getStreet());
        startingAddress.setDistrict(saveClientAdvertisementFtlRequest.getStartingAddress().getDistrict());
        startingAddress.setProvince(saveClientAdvertisementFtlRequest.getStartingAddress().getProvince());
        startingAddress.setNeighborhood(saveClientAdvertisementFtlRequest.getStartingAddress().getNeighborhood());
        startingAddress.setLat(startingAddressLtdLng.getLatitude());
        startingAddress.setLng(startingAddressLtdLng.getLongitude());

        saveClientAdvertisementFtlRequest.setStartingAddress(startingAddress);













        Set<TrailerType> trailerTypeSet = saveClientAdvertisementFtlRequest.getTrailerTypeIdList() == null || saveClientAdvertisementFtlRequest.getTrailerTypeIdList().isEmpty() ? trailerTypeService.findAll() : trailerTypeService.findSetTrailerTypesByIdList(saveClientAdvertisementFtlRequest.getTrailerTypeIdList());
        Set<TrailerFloorType> trailerFloorTypeSet = saveClientAdvertisementFtlRequest.getTrailerFloorTypeIdList() == null || saveClientAdvertisementFtlRequest.getTrailerFloorTypeIdList().isEmpty() ? trailerFloorTypeService.findAll() : trailerFloorTypeService.findSetTrailerFloorTypesByIdList(saveClientAdvertisementFtlRequest.getTrailerFloorTypeIdList());
        Set<TrailerFeature> trailerFeatureSet = saveClientAdvertisementFtlRequest.getTrailerFeatureIdList() == null || saveClientAdvertisementFtlRequest.getTrailerFeatureIdList().isEmpty() ? trailerFeatureService.findAll() :  trailerFeatureService.findSetTrailerFeaturesByIdList(saveClientAdvertisementFtlRequest.getTrailerFeatureIdList());

        Recipient startRecipient = saveClientAdvertisementFtlRequest.getStartRecipient();
        String formattedNumber = phoneFormatter.format(startRecipient.getPhoneNumber());
        startRecipient.setPhoneNumber(formattedNumber);

        Recipient dueRecipient = saveClientAdvertisementFtlRequest.getDueRecipient();
        String formattedNumber2 = phoneFormatter.format(dueRecipient.getPhoneNumber());
        dueRecipient.setPhoneNumber(formattedNumber2);

        PackagingType packagingType = packagingTypeService.findDataById(saveClientAdvertisementFtlRequest.getPackagingTypeId());

        Set<CargoType> cargoTypeSet = saveClientAdvertisementFtlRequest.getCargoTypeIdList()==null || saveClientAdvertisementFtlRequest.getCargoTypeIdList().isEmpty() ? cargoTypeService.findAll() : cargoTypeService.findSetCargoTypesByIdList(saveClientAdvertisementFtlRequest.getCargoTypeIdList());
        Set<LoadType> loadTypes = saveClientAdvertisementFtlRequest.getLoadTypeIdList()==null  || saveClientAdvertisementFtlRequest.getLoadTypeIdList().isEmpty() ? loadTypeService.findAll() : loadTypeService.findSetLoadTypeByIdList(saveClientAdvertisementFtlRequest.getLoadTypeIdList());

        CurrencyUnit currencyUnit = currencyUnitService.findDataById(saveClientAdvertisementFtlRequest.getCurrencyUnitId());
       /* if(currentClient.getAccountSetting().getCreateAdvertisementStartingAddressAutoFill()&&saveClientAdvertisementFtlRequest.getStartingAddress()==null){
            SavedAddress savedAddress= savedAddressService.findByIsDefaultAddress(Boolean.TRUE);
            AdvertisementAddress address= new AdvertisementAddress();
          /// address.setCountry(savedAddress.getCountry);
            address.setProvince(savedAddress.getProvince());
            address.setNeighborhood(savedAddress.getNeighborhood());
            address.setDistrict(savedAddress.getDistrict());
            address.setFullAddress(savedAddress.getFullAddress());
            address.setLat(savedAddress.getLat());
            address.setLng(savedAddress.getLng());
            saveClientAdvertisementFtlRequest.setStartingAddress(address);
        }*/

        SimpleAdvertisementAddress simpleStartingAddress = advertisementLogic.createSimpleAdvertisementAddress(saveClientAdvertisementFtlRequest.getStartingAddress());
        SimpleAdvertisementAddress simpleDueAddress = advertisementLogic.createSimpleAdvertisementAddress(saveClientAdvertisementFtlRequest.getDueAddress());

        Set<VehicleType> vehicleTypeSet = saveClientAdvertisementFtlRequest.getVehicleTypeIdList()==null || saveClientAdvertisementFtlRequest.getVehicleTypeIdList().isEmpty() ? vehicleTypeService.findAll() : vehicleTypeService.findSetVehicleTypesByIdList(saveClientAdvertisementFtlRequest.getVehicleTypeIdList());

        String clientAdvertisementCode=advertisementLogic.createAdvertisementCode();
        ClientAdvertisementFtl clientAdvertisementFtl= ClientAdvertisementFtl.save(
                saveClientAdvertisementFtlRequest
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
                ,vehicleTypeSet
                ,clientAdvertisementCode
        );

        clientAdvertisementFtlRepository.save(clientAdvertisementFtl);
        sendMailService.sendMailToAdminForNewAdvertisement(clientAdvertisementFtl);
        mailNotificationService.newClientAdvertisementSendMailToDriver(clientAdvertisementFtl);
        mailNotificationService.statusChangeClientAdvertisementSendMailToClient(clientAdvertisementFtl);
        pushNotificationService.newClientAdvertisementSendMobileToDriver(clientAdvertisementFtl);
        pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(clientAdvertisementFtl);

      //  firebaseMessagingService.sendNotificationToAllDriversForNewAdvertisements(clientAdvertisementFtl);
    }

    /**
     * İlanın Başlangıç veya Bitiş Tarihinin Şimdiki Zamandan Geride Olup Olmadığını Kontrol Eder.
     * İlanın Bitiş Tarihinin Başlangıç Tarihinden Önce Olup Olmadığını Kontrol Eder.
     *
     * @param saveClientAdvertisementFtlRequest İlan Kaydetme Dto Nesnesi.
     * @throws InvalidDateTimeException Zaman Hatalarını Döndürür.
     */
    @Override
    public void checkedClientAdvertisementSaveDatesOperation(SaveClientAdvertisementFtlRequest saveClientAdvertisementFtlRequest) {
        LocalDateTime startingLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementFtlRequest.getAdStartingDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementFtlRequest.getAdStartingTime()));
        LocalDateTime dueLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementFtlRequest.getAdDueDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementFtlRequest.getAdDueTime()));

        if (startingLocalDateTime.isBefore(LocalDateTime.now()) || dueLocalDateTime.isBefore(LocalDateTime.now())) {

            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementFtl.InvalidDateTimeException.notAvailable"));

        } else if (dueLocalDateTime.isBefore(startingLocalDateTime)) {
            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementFtl.InvalidDateTimeException.endTime"));
        }
    }

    /**
     * İlan Kaydetme Nesnesi İçindeki Başlangıç ve Bitiş Tarihi Arasında 120 Dakika(2 Saat) Olup Olmadığını Kontrol Eder.
     *
     * @param saveClientAdvertisementFtlRequest Client İlan Kaydetme Dto Nesnesi
     */
    @Override
    public void checkedClientAdvertisementSaveTimesOperation(SaveClientAdvertisementFtlRequest saveClientAdvertisementFtlRequest) {
        LocalDateTime startingLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementFtlRequest.getAdStartingDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementFtlRequest.getAdStartingTime()));
        LocalDateTime dueLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementFtlRequest.getAdDueDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementFtlRequest.getAdDueTime()));

        long minutes = ChronoUnit.MINUTES.between(startingLocalDateTime, dueLocalDateTime);

        if (minutes < 120) {
            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementFtl.InvalidDateTimeException.twoHour"));
        }
    }


}
