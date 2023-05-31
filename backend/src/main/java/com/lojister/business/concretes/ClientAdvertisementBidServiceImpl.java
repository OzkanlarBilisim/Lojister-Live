package com.lojister.business.concretes;

import com.lojister.business.abstracts.*;
import com.lojister.controller.advertisement.FilterClientAdvertisementRequest;
import com.lojister.core.i18n.Translator;
import com.lojister.mapper.ClientAdvertisementBidMinimalDtoMapper;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidMinimalDto;
import com.lojister.model.enums.*;
import com.lojister.core.util.annotation.ActiveDriver;
import com.lojister.core.util.annotation.DriverBoss;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.core.exception.*;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidSaveDto;
import com.lojister.mapper.ClientAdvertisementBidMapper;
import com.lojister.model.entity.SummaryAdvertisementData;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.model.entity.driver.Driver;
import com.lojister.repository.advertisement.ClientAdvertisementBidRepository;
import com.lojister.core.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ClientAdvertisementBidServiceImpl implements ClientAdvertisementBidService {

    private final ClientAdvertisementBidRepository clientAdvertisementBidRepository;
    private final ClientAdvertisementBidMapper clientAdvertisementBidMapper;
    private final AdvertisementBidLogic advertisementBidLogic;
    private final SecurityContextUtil securityContextUtil;
    private final ClientAdvertisementService clientAdvertisementService;
    @Lazy
    private final ClientTransportProcessService clientTransportProcessService;
    private final ClientAdvertisementBidMinimalDtoMapper clientAdvertisementBidMinimalDtoMapper;
    private final MailNotificationService mailNotificationService;
    private final PushNotificationService pushNotificationService;

    @Override
    public void checkedClientAdvertisementBidSaveDatesOperation(ClientAdvertisement clientAdvertisement) {

        if(Optional.ofNullable(clientAdvertisement.getAdDueDate()).isPresent()&&Optional.ofNullable(clientAdvertisement.getAdDueTime()).isPresent()){
            LocalDateTime dueLocalDateTime = LocalDateTime.of(clientAdvertisement.getAdDueDate(), clientAdvertisement.getAdDueTime());
            LocalDateTime currentLocalDateTime = LocalDateTime.now();
            if (currentLocalDateTime.isAfter(dueLocalDateTime)) {
                throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisementBid.InvalidDateTimeException"));
            }
        }

    }


    /**
     * İlan Teklifi verme
     *
     * @param clientAdvertisementBidDto teklif dto
     * @param clientAdvertisementId     ilan id'si
     * @return {@linkplain ClientAdvertisementBidDto}
     * @throws ClientAdvertisementBidUniqueConstraintException Hazırda Bekleyen Teklif Vardır.
     * @throws UnauthorizedTransactionException                Yetkisiz İşlem
     * @throws EntityNotFoundException                         İlgili Nesne Yoksa
     */
    @Override
    @ActiveDriver
    @DriverBoss
    public ClientAdvertisementBidDto save(ClientAdvertisementBidSaveDto clientAdvertisementBidDto, Long clientAdvertisementId) {





        Driver currentDriver = securityContextUtil.getCurrentDriver();

        ClientAdvertisement clientAdvertisement = clientAdvertisementService.findDataById(clientAdvertisementId);
        /*bidExpirationCheck(clientAdvertisementBidDto,clientAdvertisement);*/
        checkedClientAdvertisementBidSaveDatesOperation(clientAdvertisement);
        inWaitingStatusClientAdvertisementBidIsExist(clientAdvertisement.getId(), currentDriver.getId());

       // clientAdvertisementBidDto.setBid(advertisementBidLogic.calculateVAT(clientAdvertisementBidDto.getBid(), 2));

        SummaryAdvertisementData summaryAdvertisementData = SummaryAdvertisementData.createSummaryAdvertisementData(clientAdvertisement);

          /*clientAdvertisementBidDto.setExpiration(
                  !Optional.ofNullable(clientAdvertisementBidDto.getExpiration()).isPresent() ? LocalDateTime.parse(clientAdvertisement.getAdStartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE)+"T"+clientAdvertisement.getAdStartingTime().format(DateTimeFormatter.ISO_LOCAL_TIME),DateTimeFormatter.ISO_LOCAL_DATE_TIME)  : clientAdvertisementBidDto.getExpiration()
          );*/
        ClientAdvertisementBid clientAdvertisementBid = ClientAdvertisementBid.createClientAdvertisementBid(clientAdvertisement, summaryAdvertisementData, currentDriver, clientAdvertisementBidDto);

        clientAdvertisementBid = clientAdvertisementBidRepository.save(clientAdvertisementBid);
        mailNotificationService.clientAdvertisementBidSendMailToClient(clientAdvertisementBid);
        pushNotificationService.clientAdvertisementBidSendMobileToClient(clientAdvertisementBid);
        return clientAdvertisementBidMapper.entityToDto(clientAdvertisementBid);
    }

   /* private void bidExpirationCheck(ClientAdvertisementBidSaveDto clientAdvertisementBidDto, ClientAdvertisement clientAdvertisement) {
        if (!Optional.ofNullable(clientAdvertisementBidDto.getExpiration()).isPresent()){
            clientAdvertisementBidDto.setExpiration(LocalDateTime.parse(clientAdvertisement.getAdStartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE)+"T"+clientAdvertisement.getAdStartingTime().format(DateTimeFormatter.ISO_LOCAL_TIME)));
        }
        if(LocalDateTime.now().plusHours(2L).isAfter(clientAdvertisementBidDto.getExpiration())){
            throw new BidExpitationException(Translator.toLocale("lojister.clientAdvertisementBid.BidExpitationException"));
        }
    }*/


    @Override
    public void deleteById(Long id) {

        ClientAdvertisementBid clientAdvertisementBid = findDataById(id);
        securityContextUtil.isRoleAdminOrDriver();
        correctDriverCheck(clientAdvertisementBid);

        clientAdvertisementBidRepository.deleteById(id);

    }

    @Override
    public ClientAdvertisementBidDto getById(Long id) {

        ClientAdvertisementBid clientAdvertisementBid = findDataById(id);

        return clientAdvertisementBidMapper.entityToDto(clientAdvertisementBid);

    }


    @Override
    @OnlyAdmin
    public List<ClientAdvertisementBidDto> getAll() {
        return clientAdvertisementBidMapper.entityListToDtoList(clientAdvertisementBidRepository.findAll());
    }


    /**
     * Client için ilgili ilanına verilen teklifleri görebilme
     *
     * @param advertisementId ilan id
     * @return {@linkplain List}
     * @throws UnauthorizedTransactionException Yetkisiz İşlem
     */
    @Override
    public Page<ClientAdvertisementBidDto> getAdvertisementBidsByAdvertisementId(Long advertisementId, Pageable pageable) {

        Client currentClient = securityContextUtil.getCurrentClient();
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        ClientAdvertisement clientAdvertisement = clientAdvertisementService.findDataById(advertisementId);

        if (!currentClient.getId().equals(clientAdvertisement.getClient().getId())) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.common.UnauthorizedTransactionException"));
        }
        Page<ClientAdvertisementBid> pageableClientAdvertisementBid=clientAdvertisementBidRepository.findByClientAdvertisement_Id(advertisementId, customPageable);
       List<ClientAdvertisementBidDto> clientAdvertisementBidDtoList= clientAdvertisementBidMapper.entityListToDtoList(pageableClientAdvertisementBid.getContent());
        Page<ClientAdvertisementBidDto> pageableDtoList=new PageImpl<>(clientAdvertisementBidDtoList, pageableClientAdvertisementBid.getPageable(), pageableClientAdvertisementBid.getTotalElements());
        return pageableDtoList;

    }

    @Override
    public List<ClientAdvertisementBidDto> getAdvertisementBidsByCargoOnTheWay(FilterClientAdvertisementRequest filterClientAdvertisementRequest, String sort) {
        Driver currentDriver = securityContextUtil.getCurrentDriver();


        if (currentDriver.getRole() == Role.ROLE_DRIVER) {
            Sort s = Sort.by(Sort.Direction.DESC, sort);
            filterClientAdvertisementRequest.setClientAdvertisementTypeList(
                    filterClientAdvertisementRequest.getClientAdvertisementTypeList().isEmpty() || filterClientAdvertisementRequest.getClientAdvertisementTypeList()==null  ? Arrays.asList(ClientAdvertisementType.values()) : filterClientAdvertisementRequest.getClientAdvertisementTypeList()
            );

           return clientAdvertisementBidMapper.entityListToDtoList(clientAdvertisementBidRepository.findByDriverCargoOnTheWay(currentDriver.getId()

                   ,Arrays.asList(TransportProcessStatus.CARGO_ON_THE_WAY),
                           filterClientAdvertisementRequest.getStartingAddressProvince(),
                           filterClientAdvertisementRequest.getStartingAddressDistrict(),
                           filterClientAdvertisementRequest.getStartingAddressNeighborhood(),
                           filterClientAdvertisementRequest.getDueAddressProvince(),
                           filterClientAdvertisementRequest.getDueAddressDistrict(),
                           filterClientAdvertisementRequest.getDueAddressNeighborhood(),
                           filterClientAdvertisementRequest.getClientAdvertisementTypeList(),
                           Arrays.asList(BidStatus.TRANSPORT),
                           s
                   ));
        }
        else {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.common.UnauthorizedTransactionException"));
        }
    }

    /**
     * Driver için verdiği teklifleri görebilme
     *
     * @return {@linkplain List}
     */
    @Override
    public Page<ClientAdvertisementBidMinimalDto> getMyBids(Boolean status, Pageable pageable) {
          List<BidStatus> bidStatusList= status ? Arrays.asList(BidStatus.WAITING,BidStatus.TRANSPORT,BidStatus.CANCELLED,BidStatus.APPROVED,BidStatus.DENIED) : Arrays.asList(BidStatus.AD_CLOSED,BidStatus.TIMEOUT,BidStatus.COMPLETED);
        Driver currentDriver = securityContextUtil.getCurrentDriver();
      Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),  pageable.getSort().isEmpty() ? Sort.by("createdDateTime").descending() : pageable.getSort());
        if (currentDriver.getRole() == Role.ROLE_DRIVER) {
            Page<ClientAdvertisementBid> clientAdvertisementBidList= clientAdvertisementBidRepository.findByDriverBidder_Id(currentDriver.getId(),bidStatusList,customPageable);
            List<ClientAdvertisementBidMinimalDto> clientAdvertisementBidMinimalDtoList=clientAdvertisementBidMinimalDtoMapper.customEntityListToDtoList(clientAdvertisementBidList.getContent());
            Page<ClientAdvertisementBidMinimalDto> pageableDtoList=new PageImpl<>(clientAdvertisementBidMinimalDtoList,clientAdvertisementBidList.getPageable(),clientAdvertisementBidList.getTotalElements());
            return pageableDtoList;

        } else if (currentDriver.getRole() == Role.ROLE_DRIVER_EMPLOYEE) {

            Page<ClientTransportProcess> clientTransportProcessList = clientTransportProcessService.findByVehicleDriverIdOrderByIdDescRepo(currentDriver.getId(),bidStatusList,customPageable);

            List<ClientAdvertisementBid> clientAdvertisementBidList = new ArrayList<>();

            for (ClientTransportProcess clientTransportProcess : clientTransportProcessList) {
                clientAdvertisementBidList.add(clientTransportProcess.getAcceptedClientAdvertisementBid());
            }
              List<ClientAdvertisementBidMinimalDto> clientAdvertisementBidMinimalDtoList=clientAdvertisementBidMinimalDtoMapper.entityListToDtoList(clientAdvertisementBidList);
            Page<ClientAdvertisementBidMinimalDto> pageableDtoList=new PageImpl<>(clientAdvertisementBidMinimalDtoList,clientTransportProcessList.getPageable(),clientTransportProcessList.getTotalElements());
            return pageableDtoList;

        } else {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.common.UnauthorizedTransactionException"));
        }
    }


    /**
     * Client için verilen tekliflerden ilgili teklife verilen yanıt
     *
     * @param advertisementBidId ilan id'si
     * @param value              TRUE or FALSE
     * @throws EntityNotFoundException          İlgili Nesne Yoktur
     * @throws UnauthorizedTransactionException Yetkisiz İşlem
     * @throws DuplicateBidException            İlan Reddedildikten Sonra Veya Onaylandıktan Sonra Tekrar İstek Atılamaz.
     */
    @Override
    public void updateBidStatus(Boolean value, Long advertisementBidId) {

        ClientAdvertisementBid clientAdvertisementBid = findDataById(advertisementBidId);

        ClientAdvertisement clientAdvertisement = clientAdvertisementService.findDataById(clientAdvertisementBid.getClientAdvertisement().getId());

        correctClientCheck(clientAdvertisement);
        onlyOneBidCheck(clientAdvertisementBid);

        if (value) {

            clientAdvertisementBid.setBidStatus(BidStatus.APPROVED);
            clientAdvertisementBidRepository.save(clientAdvertisementBid);
            clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.BID_APPROVED);
            clientAdvertisement.setAdvertisementStatus(AdvertisementStatus.TRANSPORT);
            clientAdvertisementService.saveRepo(clientAdvertisement);

            //Todo burası içeri alınabilir.
            ClientTransportProcess clientTransportProcess = new ClientTransportProcess();
            clientTransportProcess.setAcceptedClientAdvertisementBid(clientAdvertisementBid);
            clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.WAITING);
            clientTransportProcess.setTransportCode(advertisementBidLogic.createTransportCode(LocalDate.now(), TransportProcessType.CLIENT_TRANSPORT_PROCESS));
            clientTransportProcess.setPrice(clientAdvertisementBid.getBid());
            clientTransportProcess.setPriceWithVat(clientAdvertisementBid.getBidWithVat());
            clientTransportProcess.setTransportProcessType(TransportProcessType.CLIENT_TRANSPORT_PROCESS);

            clientAdvertisement.setClientTransportProcess(clientTransportProcessService.saveRepo(clientTransportProcess));

             ClientAdvertisement savedClientAdvertisement= clientAdvertisementService.saveRepo(clientAdvertisement);
            mailNotificationService.statusChangeClientAdvertisementSendMailToClient(clientAdvertisement);
            pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(clientAdvertisement);
            mailNotificationService.statusChangeClientAdvertisementSendMailToDriver(clientAdvertisement);
            pushNotificationService.statusChangeClientAdvertisementSendMobileToDriver(clientAdvertisement);
            List<ClientAdvertisementBid> otherBidList = changeTheStatusOfAllBidToClosed(clientAdvertisementBid);
            clientAdvertisementBidRepository.saveAll(otherBidList);

        } else {

            clientAdvertisementBid.setBidStatus(BidStatus.DENIED);
            mailNotificationService.statusChangeClientAdvertisementBidSendMailToDriver(clientAdvertisementBid);
            pushNotificationService.statusChangeClientAdvertisementBidSendMobileToDriver(clientAdvertisementBid);
            clientAdvertisementBidRepository.save(clientAdvertisementBid);
        }
    }


    /**
     * Benim(client) ilanıma verilen tekliflerin sayısı
     *
     * @return {@linkplain Long}.
     */
    @Override
    public Long myAdvertisementBidsCount() {

        return clientAdvertisementBidRepository.countByClientAdvertisement_Client_Id(securityContextUtil.getCurrentClient().getId());
    }


    /**
     * Benim(driver), clientların ilanlarına verdiğim tekliflerin sayısı
     *
     * @return {@linkplain Long}.
     */
    @Override
    public Long myBidsCount() {
        return clientAdvertisementBidRepository.countClientAdvertisementBidByDriverBidder_Id(securityContextUtil.getCurrentDriver().getId());
    }

    @Override
    public List<ClientAdvertisementBidDto> getDriverIdAndAdvertisementProcessStatusIn(Long driverId, List<AdvertisementProcessStatus> advertisementProcessStatusList) {
        return clientAdvertisementBidMapper.entityListToDtoList(clientAdvertisementBidRepository.findByDriverBidder_IdAndClientAdvertisement_AdvertisementProcessStatusIn(driverId,advertisementProcessStatusList)) ;
    }


    /**
     * Teklif id'sinden Teklif Nesnesini Database'de Böyle Bir Teklif Nesnesinin Olmnadığı Durum da Göz Önünde Bulundurularak Null Pointer Exception Yemeden Çekilmesini Sağlar.
     * ClientAdvertisementBid Döndürür
     *
     * @param clientAdvertisementBidId Teklif Id'si
     */
    @Override
    public ClientAdvertisementBid findDataById(Long clientAdvertisementBidId) {

        Optional<ClientAdvertisementBid> clientAdvertisementBid = clientAdvertisementBidRepository.findById(clientAdvertisementBidId);

        if (clientAdvertisementBid.isPresent()) {
            return clientAdvertisementBid.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.clientAdvertisementBid.EntityNotFoundException"));
        }
    }

    @Override
    public List<ClientAdvertisementBid> findAllByClientAdvertisementIdRepo(Long clientAdvertisementId) {
        return clientAdvertisementBidRepository.findByClientAdvertisement_Id(clientAdvertisementId);
    }

    @Override
    public List<ClientAdvertisementBid> findByClientAdvertisementIdAndDriverBidderId(Long clientAdvertisementId, Long driverBidderId) {
        return clientAdvertisementBidRepository.findByClientAdvertisement_IdAndDriverBidder_Id(clientAdvertisementId, driverBidderId);
    }


    @Override
    public void onlyOneBidCheck(ClientAdvertisementBid clientAdvertisementBid) {

        if ((clientAdvertisementBid.getBidStatus() == BidStatus.DENIED) || (clientAdvertisementBid.getBidStatus() == BidStatus.APPROVED)) {
            throw new DuplicateBidException(Translator.toLocale("lojister.clientAdvertisementBid.DuplicateBidException"));
        }

    }

    @Override
    public void correctClientCheck(ClientAdvertisement clientAdvertisement) {

        if (!securityContextUtil.getCurrentClient().getId().equals(clientAdvertisement.getClient().getId())) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.common.UnauthorizedTransactionException"));
        }
    }

    @Override
    public List<ClientAdvertisementBid> changeTheStatusOfAllBidToClosed(ClientAdvertisementBid acceptedClientAdvertisementBid) {

        List<ClientAdvertisementBid> clientAdvertisementBidList = clientAdvertisementBidRepository.findByClientAdvertisement_Id(acceptedClientAdvertisementBid.getClientAdvertisement().getId());

        if (!clientAdvertisementBidList.isEmpty()) {

            for (ClientAdvertisementBid advertisementBid : clientAdvertisementBidList) {

                if (!(acceptedClientAdvertisementBid.getId().equals(advertisementBid.getId()))) {
                    advertisementBid.setBidStatus(BidStatus.AD_CLOSED);
                }
            }
            return clientAdvertisementBidList;
        } else {
            throw new EmptyAdvertisementBidException(Translator.toLocale("lojister.clientAdvertisementBid.EmptyAdvertisementBidException"));
        }
    }

    @Override
    public List<ClientAdvertisementBid> checkAdvertisementExpiration(List<ClientAdvertisementBid> clientAdvertisementBidList) {

        return null;
    }

    /**
     * Teklifi Veren Sürücünün Şuanki(Login) Sürücü Olup Olmadığının Kontrolü Yapılır
     *
     * @param clientAdvertisementBid Teklif nesnesi
     */
    @Override
    public void correctDriverCheck(ClientAdvertisementBid clientAdvertisementBid) {

        if (!(clientAdvertisementBid.getDriverBidder().getId().equals(securityContextUtil.getCurrentUserId()))) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.clientAdvertisementBid.UnauthorizedTransactionException"));
        }
    }

    /**
     * İlgili İlana Sürücünün Hali Hazırda Verdiği Başka Teklif Var Mı Yok Mu Kontrol İçin Yazılmıştır.
     *
     * @param clientAdvertisementId İlan Id'si
     * @param currentDriverId       Şuanki(Login Olan) Sürücünün Id'si
     * @throws ClientAdvertisementBidUniqueConstraintException Hazırda Bekleyen Teklif Vardır.
     */
    @Override
    public void inWaitingStatusClientAdvertisementBidIsExist(Long clientAdvertisementId, Long currentDriverId) {

        Optional<ClientAdvertisementBid> clientAdvertisementBidData = clientAdvertisementBidRepository.findByClientAdvertisement_IdAndDriverBidder_IdAndBidStatus(clientAdvertisementId, currentDriverId, BidStatus.WAITING);

        if (clientAdvertisementBidData.isPresent()) {
            throw new ClientAdvertisementBidUniqueConstraintException(Translator.toLocale("lojister.clientAdvertisementBid.ClientAdvertisementBidUniqueConstraintException"));
        }
    }

    @Override
    public void delete(ClientAdvertisementBid clientAdvertisementBid) {

        clientAdvertisementBidRepository.deleteById(clientAdvertisementBid.getId());

    }

    @Override
    public ClientAdvertisementBid saveRepo(ClientAdvertisementBid clientAdvertisementBid) {
        return clientAdvertisementBidRepository.save(clientAdvertisementBid);
    }

    @Override
    public Long countByDriverIdAndInBidStatus(List<BidStatus> bidStatusList) {
        Driver driver = securityContextUtil.getCurrentDriver();
        return clientAdvertisementBidRepository.countByDriver_IdAndInBidStatus(driver.getId(),bidStatusList);
    }


}
