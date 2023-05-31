package com.lojister.business.concretes;

import com.lojister.business.abstracts.*;
import com.lojister.business.abstracts.dynamic.*;
import com.lojister.business.concretes.dynamic.TrailerTypeServiceImpl;
import com.lojister.controller.advertisement.SaveClientAdvertisementContainerRequest;
import com.lojister.controller.advertisement.SaveClientAdvertisementFtlRequest;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.exception.InvalidDateTimeException;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.LocalDateTimeParseUtil;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.validator.formatter.PhoneFormatter;
import com.lojister.mapper.ClientAdvertisementContainerDtoMapper;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementContainerDto;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.SavedAddress;
import com.lojister.model.entity.SimpleAdvertisementAddress;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisementContainer;
import com.lojister.model.entity.client.ClientAdvertisementFtl;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.repository.advertisement.ClientAdvertisementContainerRepository;
import com.lojister.service.firebase.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientAdvertisementContainerManager implements ClientAdvertisementContainerService {
  private final ClientAdvertisementContainerRepository clientAdvertisementContainerRepository;
  private final ClientAdvertisementContainerDtoMapper clientAdvertisementContainerDtoMapper;
  private final SecurityContextUtil securityContextUtil;
  private final CurrencyUnitService currencyUnitService;
  private final LocalDateTimeParseUtil localDateTimeParseUtil;
  private final PhoneFormatter phoneFormatter;
  private final SendMailService sendMailService;
  private final AdvertisementLogic advertisementLogic;
  private  final MailNotificationService mailNotificationService;
  private final PushNotificationService pushNotificationService;
    @Override
    public ClientAdvertisementContainerDto save(SaveClientAdvertisementContainerRequest saveClientAdvertisementContainerRequest) {

      Client currentClient = securityContextUtil.getCurrentClient();
      //Date Check Function
      checkedClientAdvertisementSaveDatesOperation(saveClientAdvertisementContainerRequest);
      checkedClientAdvertisementSaveTimesOperation(saveClientAdvertisementContainerRequest);

      Recipient startRecipient = saveClientAdvertisementContainerRequest.getStartRecipient();
      String formattedNumber = phoneFormatter.format(startRecipient.getPhoneNumber());
      startRecipient.setPhoneNumber(formattedNumber);

      Recipient dueRecipient = saveClientAdvertisementContainerRequest.getDueRecipient();
      String formattedNumber2 = phoneFormatter.format(dueRecipient.getPhoneNumber());
      dueRecipient.setPhoneNumber(formattedNumber2);

      Recipient containerRecipient = saveClientAdvertisementContainerRequest.getContainerRecipient();
      String formattedNumber3 = phoneFormatter.format(dueRecipient.getPhoneNumber());
      dueRecipient.setPhoneNumber(formattedNumber3);

      CurrencyUnit currencyUnit = currencyUnitService.findDataById(saveClientAdvertisementContainerRequest.getCurrencyUnitId());
      SimpleAdvertisementAddress simpleStartingAddress = advertisementLogic.createSimpleAdvertisementAddress(saveClientAdvertisementContainerRequest.getStartingAddress());
      SimpleAdvertisementAddress simpleDueAddress = advertisementLogic.createSimpleAdvertisementAddress(saveClientAdvertisementContainerRequest.getDueAddress());
      SimpleAdvertisementAddress simpleContainerAddress=advertisementLogic.createSimpleAdvertisementAddress(saveClientAdvertisementContainerRequest.getContainerAddress());
      String clientAdvertisementCode=advertisementLogic.createAdvertisementCode();
      ClientAdvertisementContainer clientAdvertisementContainer= ClientAdvertisementContainer.save(
              saveClientAdvertisementContainerRequest
              ,currentClient
              ,startRecipient
              ,dueRecipient
              ,containerRecipient
              ,simpleStartingAddress
              ,simpleDueAddress
              ,simpleContainerAddress
              ,clientAdvertisementCode
              ,currencyUnit
      );

      ClientAdvertisementContainer savedClientAdvertisementContainer= clientAdvertisementContainerRepository.save(clientAdvertisementContainer);
      sendMailService.sendMailToAdminForNewAdvertisement(clientAdvertisementContainer);
      mailNotificationService.newClientAdvertisementSendMailToDriver(clientAdvertisementContainer);
      mailNotificationService.statusChangeClientAdvertisementSendMailToClient(clientAdvertisementContainer);
      pushNotificationService.newClientAdvertisementSendMobileToDriver(clientAdvertisementContainer);
      pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(clientAdvertisementContainer);
      return clientAdvertisementContainerDtoMapper.entityToDto(savedClientAdvertisementContainer);// clientAdvertisementContainerDtoMapper.entityToDto();
    }


  @Override
  public ClientAdvertisementContainer findById(Long id) {
    return clientAdvertisementContainerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  /**
   * İlanın Başlangıç veya Bitiş Tarihinin Şimdiki Zamandan Geride Olup Olmadığını Kontrol Eder.
   * İlanın Bitiş Tarihinin Başlangıç Tarihinden Önce Olup Olmadığını Kontrol Eder.
   *
   * @param saveClientAdvertisementContainerRequest İlan Kaydetme Dto Nesnesi.
   * @throws InvalidDateTimeException Zaman Hatalarını Döndürür.
   */
  @Override
  public void checkedClientAdvertisementSaveDatesOperation(SaveClientAdvertisementContainerRequest saveClientAdvertisementContainerRequest) {
    LocalDateTime startingLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementContainerRequest.getAdStartingDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementContainerRequest.getAdStartingTime()));
    LocalDateTime dueLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementContainerRequest.getAdDueDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementContainerRequest.getAdDueTime()));

    if (startingLocalDateTime.isBefore(LocalDateTime.now()) || dueLocalDateTime.isBefore(LocalDateTime.now())) {

      throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementContainer.InvalidDateTimeException.notAvailable"));

    } else if (dueLocalDateTime.isBefore(startingLocalDateTime)) {
      throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementContainer.InvalidDateTimeException.endTime"));
    }
  }

  /**
   * İlan Kaydetme Nesnesi İçindeki Başlangıç ve Bitiş Tarihi Arasında 120 Dakika(2 Saat) Olup Olmadığını Kontrol Eder.
   *
   * @param saveClientAdvertisementContainerRequest Client İlan Kaydetme Dto Nesnesi
   */
  @Override
  public void checkedClientAdvertisementSaveTimesOperation(SaveClientAdvertisementContainerRequest saveClientAdvertisementContainerRequest) {
    LocalDateTime startingLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementContainerRequest.getAdStartingDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementContainerRequest.getAdStartingTime()));
    LocalDateTime dueLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementContainerRequest.getAdDueDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementContainerRequest.getAdDueTime()));

    long minutes = ChronoUnit.MINUTES.between(startingLocalDateTime, dueLocalDateTime);

    if (minutes < 120) {
      throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementContainer.InvalidDateTimeException.twoHour"));
    }
  }


}
