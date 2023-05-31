package com.lojister.business.concretes;

import com.lojister.business.abstracts.ClientAdvertisementBidService;
import com.lojister.business.abstracts.ClientAdvertisementService;
import com.lojister.controller.advertisement.FilterClientAdvertisementRequest;
import com.lojister.controller.advertisement.SaveAdvertisementExcelRequest;
import com.lojister.controller.advertisement.SaveClientAdvertisementRequest;
import com.lojister.core.exception.*;
import com.lojister.core.helper.ExcelHelper;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.LocalDateTimeParseUtil;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.util.TempFileUtil;
import com.lojister.core.util.annotation.ConfirmedClient;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.mapper.*;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementMinimalDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementSimpleDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.entity.client.*;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.enums.*;
import com.lojister.repository.advertisement.ClientAdvertisementRepository;
import com.lojister.repository.common.spec.SpecificationUtil;
import com.lojister.repository.common.spec.JoinColumnProps;
import com.lojister.repository.common.spec.SearchFilter;
import com.lojister.repository.common.spec.SearchQuery;

import com.lojister.service.osrm.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ClientAdvertisementServiceImpl implements ClientAdvertisementService {

    private final ClientAdvertisementRepository clientAdvertisementRepository;
    private final ClientAdvertisementBidMapper clientAdvertisementBidMapper;
    private final SecurityContextUtil securityContextUtil;
    private final LocalDateTimeParseUtil localDateTimeParseUtil;
    private final ExcelHelper excelHelper;
    @Lazy
    private final ClientAdvertisementBidService clientAdvertisementBidService;
    private final ClientAdvertisementFtlMapper clientAdvertisementFtlMapper;
    private final ClientAdvertisementPartialMapper clientAdvertisementPartialMapper;

    private final ClientAdvertisementSimpleDtoMapper clientAdvertisementSimpleDtoMapper;
    private final ClientAdvertisementMinimalDtoMapper clientAdvertisementMinimalDtoMapper;
    private final ClientAdvertisementContainerDtoMapper clientAdvertisementContainerDtoMapper;


    //ClientAdvertisementType cast işlemi
    //---------------------------------------------------
    private List<ClientAdvertisementDto> converterClientAdvertisementList(List<ClientAdvertisement> clientAdvertisementList) {
        List<ClientAdvertisementDto> clientAdvertisementDtoList = new ArrayList<>();
        clientAdvertisementList.stream().forEach((clientAdvertisement) -> {
            if (clientAdvertisement.getClientAdvertisementType() == ClientAdvertisementType.FTL) {
                clientAdvertisementDtoList.add(clientAdvertisementFtlMapper.entityToDto((ClientAdvertisementFtl) clientAdvertisement));

            }
            if (clientAdvertisement.getClientAdvertisementType() == ClientAdvertisementType.PARTIAL) {
                clientAdvertisementDtoList.add(clientAdvertisementPartialMapper.entityToDto((ClientAdvertisementPartial) clientAdvertisement));
            }

        });
        return clientAdvertisementDtoList;
    }

    private ClientAdvertisementDto converterClientAdvertisement(ClientAdvertisement clientAdvertisement) {
        ClientAdvertisementDto clientAdvertisementDto = new ClientAdvertisementDto();

        if (clientAdvertisement.getClientAdvertisementType() == ClientAdvertisementType.FTL) {
            clientAdvertisementDto = clientAdvertisementFtlMapper.entityToDto((ClientAdvertisementFtl) clientAdvertisement);

        }
        if (clientAdvertisement.getClientAdvertisementType() == ClientAdvertisementType.PARTIAL) {
            clientAdvertisementDto = clientAdvertisementPartialMapper.entityToDto((ClientAdvertisementPartial) clientAdvertisement);
        }
        if (clientAdvertisement.getClientAdvertisementType() == ClientAdvertisementType.CONTAINER) {
            clientAdvertisementDto = clientAdvertisementContainerDtoMapper.customEntity((ClientAdvertisementContainer) clientAdvertisement);
        }

        return clientAdvertisementDto;
    }

    //-----------------------------------------------------
    @Override
    @ConfirmedClient
    public ClientAdvertisementDto save(SaveClientAdvertisementRequest saveClientAdvertisementRequest) {

        return null;

    }


    @Override
    public ClientAdvertisementDto getById(Long id) {
        return converterClientAdvertisement(findDataById(id));
    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        ClientAdvertisement clientAdvertisement = findDataById(id);

        deleteClientAdvertisementIsFinished(clientAdvertisement);

        List<ClientAdvertisementBid> clientAdvertisementBids = clientAdvertisementBidService.findAllByClientAdvertisementIdRepo(clientAdvertisement.getId());

        if (!clientAdvertisementBids.isEmpty()) {

            for (ClientAdvertisementBid clientAdvertisementBid : clientAdvertisementBids) {

                clientAdvertisementBidService.delete(clientAdvertisementBid);
            }
        }

        clientAdvertisementRepository.deleteById(id);

    }

    @Override
    @OnlyAdmin
    public ClientAdvertisementDto hideAdvertisement(Long clientAdvertisementId) {

        ClientAdvertisement clientAdvertisement = findDataById(clientAdvertisementId);


        if (clientAdvertisement.getAdvertisementStatus() == AdvertisementStatus.FINISHED) {
            throw new AdvertisementStatusException(Translator.toLocale("lojister.clientAdvertisement.AdvertisementStatusException.hide"));
        }

        clientAdvertisement.setAdvertisementStatus(AdvertisementStatus.HIDDEN);

        return converterClientAdvertisement(clientAdvertisementRepository.save(clientAdvertisement));

    }

    @Override
    @OnlyAdmin
    public ClientAdvertisementDto activateAdvertisement(Long clientAdvertisementId) {

        ClientAdvertisement clientAdvertisement = findDataById(clientAdvertisementId);


        if (clientAdvertisement.getAdvertisementStatus() == AdvertisementStatus.FINISHED) {
            throw new AdvertisementStatusException(Translator.toLocale("lojister.clientAdvertisement.AdvertisementStatusException.active"));
        }

        clientAdvertisement.setAdvertisementStatus(AdvertisementStatus.ACTIVE);
        return converterClientAdvertisement(clientAdvertisementRepository.save(clientAdvertisement));

    }


    @Override
    public List<ClientAdvertisementDto> getAll() {

        return converterClientAdvertisementList(clientAdvertisementRepository.findAll());
    }

    @Override
    public List<ClientAdvertisementSimpleDto> getAllButSimple() {
        return clientAdvertisementSimpleDtoMapper.entityListToDtoList(clientAdvertisementRepository.getAllSimple());
    }

    @Override
    public List<ClientAdvertisementMinimalDto> getAdvertisementFilterAndSort(FilterClientAdvertisementRequest filterClientAdvertisementRequest, String sort) {
        Sort s = Sort.by(Sort.Direction.DESC, sort);

        filterClientAdvertisementRequest.setClientAdvertisementTypeList(
                filterClientAdvertisementRequest.getClientAdvertisementTypeList().isEmpty() ? Arrays.asList(ClientAdvertisementType.values()) : filterClientAdvertisementRequest.getClientAdvertisementTypeList()
        );
        filterClientAdvertisementRequest.setRegionAdvertisementTypeList(
                Optional.ofNullable(filterClientAdvertisementRequest.getRegionAdvertisementTypeList()).isPresent() ? filterClientAdvertisementRequest.getRegionAdvertisementTypeList() : Arrays.asList(RegionAdvertisementType.DOMESTIC)
        );
        List<SearchFilter> searchFilters = new ArrayList<>();
        List<JoinColumnProps> joinColumnProps = new ArrayList<>();
        SearchQuery searchQuery = new SearchQuery();
        searchFilters.add(SearchFilter.builder().field("advertisementStatus").operator(QueryOperator.IN).values(Arrays.asList(AdvertisementStatus.ACTIVE.toString())).build());
        searchFilters.add(SearchFilter.builder().field("clientAdvertisementType").operator(QueryOperator.IN).values(filterClientAdvertisementRequest.getClientAdvertisementTypeList().stream().map((advertisementType) -> advertisementType.toString()).collect(Collectors.toList())).build());
        searchFilters.add(SearchFilter.builder().field("regionAdvertisementType").operator(QueryOperator.IN).values(filterClientAdvertisementRequest.getRegionAdvertisementTypeList().stream().map((regionAdvertisementType) -> regionAdvertisementType.toString()).collect(Collectors.toList())).build());
        searchQuery.setSearchFitler(searchFilters);

        if (Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressCountry()).isPresent()&&filterClientAdvertisementRequest.getStartingAddressCountry()!="") {
            joinColumnProps.add(JoinColumnProps.builder().joinColumnName("startingAddress").searchFilter(
                    SearchFilter.builder().field("country").operator(QueryOperator.LIKE).value(filterClientAdvertisementRequest.getStartingAddressCountry()).build()
            ).build());
        }
        if (Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressProvince()).isPresent()&&filterClientAdvertisementRequest.getStartingAddressProvince()!="") {
            joinColumnProps.add(JoinColumnProps.builder().joinColumnName("startingAddress").searchFilter(
                    SearchFilter.builder().field("province").operator(QueryOperator.LIKE).value(filterClientAdvertisementRequest.getStartingAddressProvince()).build()
            ).build());
        }
        if (Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressDistrict()).isPresent()&&filterClientAdvertisementRequest.getStartingAddressDistrict()!="") {
            joinColumnProps.add(JoinColumnProps.builder().joinColumnName("startingAddress").searchFilter(
                    SearchFilter.builder().field("district").operator(QueryOperator.LIKE).value(filterClientAdvertisementRequest.getStartingAddressDistrict()).build()
            ).build());
        }
        if (Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressNeighborhood()).isPresent()&&filterClientAdvertisementRequest.getStartingAddressNeighborhood()!="") {
            joinColumnProps.add(JoinColumnProps.builder().joinColumnName("startingAddress").searchFilter(
                    SearchFilter.builder().field("neighborhood").operator(QueryOperator.LIKE).value(filterClientAdvertisementRequest.getStartingAddressNeighborhood()).build()
            ).build());

        }
        if (Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressCountry()).isPresent()&&filterClientAdvertisementRequest.getDueAddressCountry()!="") {
            joinColumnProps.add(JoinColumnProps.builder().joinColumnName("dueAddress").searchFilter(
                    SearchFilter.builder().field("country").operator(QueryOperator.LIKE).value(filterClientAdvertisementRequest.getDueAddressCountry()).build()
            ).build());
        }
        if (Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressProvince()).isPresent()&&filterClientAdvertisementRequest.getDueAddressProvince()!="") {
            joinColumnProps.add(JoinColumnProps.builder().joinColumnName("dueAddress").searchFilter(
                    SearchFilter.builder().field("province").operator(QueryOperator.LIKE).value(filterClientAdvertisementRequest.getDueAddressProvince()).build()
            ).build());
        }
        if (Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressDistrict()).isPresent()&&filterClientAdvertisementRequest.getDueAddressDistrict()!="") {
            joinColumnProps.add(JoinColumnProps.builder().joinColumnName("dueAddress").searchFilter(
                    SearchFilter.builder().field("district").operator(QueryOperator.LIKE).value(filterClientAdvertisementRequest.getDueAddressDistrict()).build()
            ).build());
        }
        if (Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressNeighborhood()).isPresent()&&filterClientAdvertisementRequest.getDueAddressNeighborhood()!="") {
            joinColumnProps.add(JoinColumnProps.builder().joinColumnName("dueAddress").searchFilter(
                    SearchFilter.builder().field("neighborhood").operator(QueryOperator.LIKE).value(filterClientAdvertisementRequest.getDueAddressNeighborhood()).build()
            ).build());
        }

        searchQuery.setJoinColumnProps(joinColumnProps);

        if (securityContextUtil.IsAnonymousUser()) {

            return clientAdvertisementMinimalDtoMapper
                    .customEntityListToDtoList(clientAdvertisementRepository.findAll(SpecificationUtil.bySearchQuery(searchQuery, ClientAdvertisement.class), s));
        } else {

            return clientAdvertisementMinimalDtoMapper
                    .entityListToDtoList(clientAdvertisementRepository.findAll(SpecificationUtil.bySearchQuery(searchQuery, ClientAdvertisement.class), s));
        }










      /*  if(securityContextUtil.IsAnonymousUser()){
            return clientAdvertisementMinimalDtoMapper
                    .customEntityListToDtoList(clientAdvertisementRepository.getAdvertisementFilterAndSort(Arrays.asList(
                                    AdvertisementStatus.ACTIVE
                            ), filterClientAdvertisementRequest.getStartingAddressProvince(),
                            filterClientAdvertisementRequest.getStartingAddressDistrict(),
                            filterClientAdvertisementRequest.getStartingAddressNeighborhood(),
                            filterClientAdvertisementRequest.getDueAddressProvince(),
                            filterClientAdvertisementRequest.getDueAddressDistrict(),
                            filterClientAdvertisementRequest.getDueAddressNeighborhood(),
                            filterClientAdvertisementRequest.getClientAdvertisementTypeList(),
                            filterClientAdvertisementRequest.getRegionAdvertisementTypeList(),
                            s
                    ));
        }
       else {

            return clientAdvertisementMinimalDtoMapper
                    .entityListToDtoList(clientAdvertisementRepository.getAdvertisementFilterAndSort(Arrays.asList(
                                    AdvertisementStatus.ACTIVE
                            ), filterClientAdvertisementRequest.getStartingAddressProvince(),
                            filterClientAdvertisementRequest.getStartingAddressDistrict(),
                            filterClientAdvertisementRequest.getStartingAddressNeighborhood(),
                            filterClientAdvertisementRequest.getDueAddressProvince(),
                            filterClientAdvertisementRequest.getDueAddressDistrict(),
                            filterClientAdvertisementRequest.getDueAddressNeighborhood(),
                            filterClientAdvertisementRequest.getClientAdvertisementTypeList(),
                            filterClientAdvertisementRequest.getRegionAdvertisementTypeList(),
                            s
                    ));
        }*/

    }

    @Override
    public List<ClientAdvertisementDto> getFinishedAdvertisement() {

        return converterClientAdvertisementList(clientAdvertisementRepository
                .findByAdvertisementStatusOrderByIdDesc(AdvertisementStatus.FINISHED));
    }

    @Override
    public List<ClientAdvertisementDto> getHiddenAdvertisement() {


        return converterClientAdvertisementList(clientAdvertisementRepository
                .findByAdvertisementStatusOrderByIdDesc(AdvertisementStatus.HIDDEN));
    }


    @Override
    public List<ClientAdvertisementDto> getActiveAdvertisement() {

        return converterClientAdvertisementList(clientAdvertisementRepository.findByAdvertisementStatusOrderByIdDesc(AdvertisementStatus.ACTIVE));
    }

    @Override
    public List<ClientAdvertisementDto> getTransportAdvertisement() {

        return converterClientAdvertisementList(clientAdvertisementRepository.findByAdvertisementStatusOrderByIdDesc(AdvertisementStatus.TRANSPORT));
    }

    @Override
    public List<ClientAdvertisementMinimalDto> getCargoOnTheWayAdvertisement(FilterClientAdvertisementRequest filterClientAdvertisementRequest, String sort) {
        Sort s = Sort.by(Sort.Direction.DESC, sort);
        Client client = securityContextUtil.getCurrentClient();
        filterClientAdvertisementRequest.setClientAdvertisementTypeList(
                filterClientAdvertisementRequest.getClientAdvertisementTypeList().isEmpty() ? Arrays.asList(ClientAdvertisementType.values()) : filterClientAdvertisementRequest.getClientAdvertisementTypeList()
        );

        return clientAdvertisementMinimalDtoMapper
                .entityListToDtoList(clientAdvertisementRepository.getCargoOnTheWayAdvertisement(client.getId(), Arrays.asList(
                                AdvertisementProcessStatus.CARGO_ON_THE_WAY
                        ), filterClientAdvertisementRequest.getStartingAddressProvince(),
                        filterClientAdvertisementRequest.getStartingAddressDistrict(),
                        filterClientAdvertisementRequest.getStartingAddressNeighborhood(),
                        filterClientAdvertisementRequest.getDueAddressProvince(),
                        filterClientAdvertisementRequest.getDueAddressDistrict(),
                        filterClientAdvertisementRequest.getDueAddressNeighborhood(),
                        filterClientAdvertisementRequest.getClientAdvertisementTypeList(),
                        s
                ));

    }


    @Override
    public List<ClientAdvertisementDto> getMyAdvertisement() {
        List<ClientAdvertisementDto> clientAdvertisementDtoList = converterClientAdvertisementList(clientAdvertisementRepository.findByClient_IdOrderByIdDesc(securityContextUtil.getCurrentClient().getId()));
        return converterClientAdvertisementList(clientAdvertisementRepository.findByClient_IdOrderByIdDesc(securityContextUtil.getCurrentClient().getId()));
    }

    @Override
    public List<ClientAdvertisementDto> getMyAdvertisementByProvincesAndDateRange(String startProvince, String finishProvince, String startDate, String endDate) {


        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {

            Client currentClient = securityContextUtil.getCurrentClient();

            if (StringUtils.isBlank(startProvince) && StringUtils.isBlank(finishProvince)) {

                return converterClientAdvertisementList(clientAdvertisementRepository
                        .findMyAdvertisementByDateRange(localDateTimeParseUtil.getLocalDate(startDate),
                                localDateTimeParseUtil.getLocalDate(endDate), currentClient.getId(),
                                AdvertisementStatus.FINISHED));

            } else if (StringUtils.isBlank(finishProvince)) {

                return converterClientAdvertisementList(clientAdvertisementRepository
                        .findMyAdvertisementByStartingAddress_ProvinceAndDateRange(localDateTimeParseUtil.getLocalDate(startDate),
                                localDateTimeParseUtil.getLocalDate(endDate), startProvince, currentClient.getId(),
                                AdvertisementStatus.FINISHED));

            } else if (StringUtils.isBlank(startProvince)) {

                return converterClientAdvertisementList(clientAdvertisementRepository.
                        findMyAdvertisementByDueAddress_ProvinceAndDateRange(localDateTimeParseUtil.getLocalDate(startDate),
                                localDateTimeParseUtil.getLocalDate(endDate), finishProvince, currentClient.getId(),
                                AdvertisementStatus.FINISHED));

            }

            return converterClientAdvertisementList(clientAdvertisementRepository.
                    findMyAdvertisementByDateRangeAndProvince(localDateTimeParseUtil.getLocalDate(startDate),
                            localDateTimeParseUtil.getLocalDate(endDate), startProvince, finishProvince, currentClient.getId(),
                            AdvertisementStatus.FINISHED));
        } else {
            throw new EmptyStringException(Translator.toLocale("lojister.clientAdvertisement.EmptyStringException"));
        }

    }

    @Override
    public List<ClientAdvertisementDto> getPartialAdvertisement() {
        return converterClientAdvertisementList(clientAdvertisementRepository.
                findByClientAdvertisementType(ClientAdvertisementType.PARTIAL));
    }

    @Override
    public List<ClientAdvertisementDto> getFTLAdvertisement() {
        return converterClientAdvertisementList(clientAdvertisementRepository.findByClientAdvertisementType(ClientAdvertisementType.FTL));
    }

    @Override
    public List<ClientAdvertisementDto> getByVehicleTypeIdList(List<Long> vehicleTypeIdList) {
        return converterClientAdvertisementList(clientAdvertisementRepository.findByVehicleTypeIdList(vehicleTypeIdList));
    }

    @Override
    public List<ClientAdvertisementDto> getByTrailerTypeIdList(List<Long> trailerTypeIdList) {
        return converterClientAdvertisementList(clientAdvertisementRepository.findByTrailerTypeIdList(trailerTypeIdList));
    }

    @Override
    public List<ClientAdvertisementDto> getByTrailerFloorTypeIdList(List<Long> trailerFloorTypeIdList) {
        return converterClientAdvertisementList(clientAdvertisementRepository.findByTrailerFloorTypeIdList(trailerFloorTypeIdList));
    }

    @Override
    public List<ClientAdvertisementDto> getByTrailerFeatureIdList(List<Long> trailerFeatureIdList) {
        return converterClientAdvertisementList(clientAdvertisementRepository.findByTrailerFeatureIdList(trailerFeatureIdList));
    }


    @Override
    public List<ClientAdvertisementDto> getByProvincesAndDateRange(String startDate, String endDate, String startProvince, String dueProvince) {


        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {

            LocalDate startLocalDate = LocalDate.now().minusYears(1);
            LocalDate endLocalDate = LocalDate.now().plusYears(1);

            if (StringUtils.isBlank(startProvince) && StringUtils.isBlank(dueProvince)) {
                return converterClientAdvertisementList(clientAdvertisementRepository.findByDateRange(startLocalDate, endLocalDate, AdvertisementStatus.ACTIVE));
            } else if (StringUtils.isBlank(dueProvince)) {

                return converterClientAdvertisementList(clientAdvertisementRepository.findByStartingAddress_ProvinceAndDateRange(startLocalDate, endLocalDate, startProvince, AdvertisementStatus.ACTIVE));

            } else if (StringUtils.isBlank(startProvince)) {

                return converterClientAdvertisementList(clientAdvertisementRepository.findByDueAddress_ProvinceAndDateRange(startLocalDate, endLocalDate, dueProvince, AdvertisementStatus.ACTIVE));
            }

            return converterClientAdvertisementList(clientAdvertisementRepository.findByDateRangeAndProvince(startLocalDate, endLocalDate, startProvince, dueProvince, AdvertisementStatus.ACTIVE));
        } else {

            if (StringUtils.isBlank(startProvince) && StringUtils.isBlank(dueProvince)) {

                return converterClientAdvertisementList(clientAdvertisementRepository.findByDateRange(localDateTimeParseUtil.getLocalDate(startDate), localDateTimeParseUtil.getLocalDate(endDate), AdvertisementStatus.ACTIVE));

            } else if (StringUtils.isBlank(dueProvince)) {

                return converterClientAdvertisementList(clientAdvertisementRepository.findByStartingAddress_ProvinceAndDateRange(localDateTimeParseUtil.getLocalDate(startDate), localDateTimeParseUtil.getLocalDate(endDate), startProvince, AdvertisementStatus.ACTIVE));

            } else if (StringUtils.isBlank(startProvince)) {

                return converterClientAdvertisementList(clientAdvertisementRepository.findByDueAddress_ProvinceAndDateRange(localDateTimeParseUtil.getLocalDate(startDate), localDateTimeParseUtil.getLocalDate(endDate), dueProvince, AdvertisementStatus.ACTIVE));

            }

            return converterClientAdvertisementList(clientAdvertisementRepository.findByDateRangeAndProvince(localDateTimeParseUtil.getLocalDate(startDate), localDateTimeParseUtil.getLocalDate(endDate), startProvince, dueProvince, AdvertisementStatus.ACTIVE));

        }

    }

    @Override
    public Long getActiveAdvertisementCount() {
        return clientAdvertisementRepository.countByAdvertisementStatus(AdvertisementStatus.ACTIVE);
    }

    @Override
    public Long getHiddenAdvertisementCount() {
        return clientAdvertisementRepository.countByAdvertisementStatus(AdvertisementStatus.HIDDEN);
    }

    @Override
    public Long getFinishedAdvertisementCount() {
        return clientAdvertisementRepository.countByAdvertisementStatus(AdvertisementStatus.FINISHED);
    }

    @Override
    public Long getMyActiveAdvertisementCount() {
        return clientAdvertisementRepository.countByAdvertisementStatusAndClient_Id(AdvertisementStatus.ACTIVE, securityContextUtil.getCurrentClient().getId());
    }


    @Override
    public List<ClientAdvertisementBidDto> getClientAdvertisementBidsByClientAdvertisementId(Long clientAdvertisementId) {

        Driver currentDriver = securityContextUtil.getCurrentDriver();

        List<ClientAdvertisementBid> clientAdvertisementBidList = clientAdvertisementBidService.findByClientAdvertisementIdAndDriverBidderId(clientAdvertisementId, currentDriver.getId());

        return clientAdvertisementBidMapper.entityListToDtoList(clientAdvertisementBidList);
    }


    @Override
    public List<SaveAdvertisementExcelRequest> uploadFile(FileUploadUtil.FileResult result) {

        try (TempFileUtil.TempFileResult tempFileResult = TempFileUtil.cache(result.getFilename(), result.getFileStream())) {

            Blob blob = BlobProxy.generateProxy(tempFileResult.getFileInputStream(), tempFileResult.getLength());

            return excelHelper.excelToClientAdvertisement(blob.getBinaryStream());


        } catch (Exception e) {
            // throw new ExcelException("Excel dosyası yüklenirken hata oluştu : " + e.getMessage());
            throw new ExcelException(Translator.toLocale("lojister.clientAdvertisement.ExcelException"));
        }
    }

    /**
     * İlanın Başlangıç veya Bitiş Tarihinin Şimdiki Zamandan Geride Olup Olmadığını Kontrol Eder.
     * İlanın Bitiş Tarihinin Başlangıç Tarihinden Önce Olup Olmadığını Kontrol Eder.
     *
     * @param saveClientAdvertisementRequest İlan Kaydetme Dto Nesnesi.
     * @throws InvalidDateTimeException Zaman Hatalarını Döndürür.
     */
    @Override
    public void checkedClientAdvertisementSaveDatesOperation(SaveClientAdvertisementRequest saveClientAdvertisementRequest) {

        LocalDateTime startingLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementRequest.getAdStartingDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementRequest.getAdStartingTime()));
        LocalDateTime dueLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementRequest.getAdDueDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementRequest.getAdDueTime()));

        if (startingLocalDateTime.isBefore(LocalDateTime.now()) || dueLocalDateTime.isBefore(LocalDateTime.now())) {

            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisement.InvalidDateTimeException.notAvailable"));

        } else if (dueLocalDateTime.isBefore(startingLocalDateTime)) {
            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisement.InvalidDateTimeException.endTime"));
        }
    }

    /**
     * İlan Kaydetme Nesnesi İçindeki Başlangıç ve Bitiş Tarihi Arasında 120 Dakika(2 Saat) Olup Olmadığını Kontrol Eder.
     *
     * @param saveClientAdvertisementRequest Client İlan Kaydetme Dto Nesnesi
     */
    @Override
    public void checkedClientAdvertisementSaveTimesOperation(SaveClientAdvertisementRequest saveClientAdvertisementRequest) {

        LocalDateTime startingLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementRequest.getAdStartingDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementRequest.getAdStartingTime()));
        LocalDateTime dueLocalDateTime = LocalDateTime.of(localDateTimeParseUtil.getLocalDate(saveClientAdvertisementRequest.getAdDueDate()), localDateTimeParseUtil.getLocalTime(saveClientAdvertisementRequest.getAdDueTime()));

        long minutes = ChronoUnit.MINUTES.between(startingLocalDateTime, dueLocalDateTime);

        if (minutes < 120) {
            throw new InvalidDateTimeException(Translator.toLocale("lojister.clientAdvertisement.InvalidDateTimeException.twoHour"));
        }
    }


    /**
     * İlan Tipinin PARTIAL ya da FTL olup olmadığını kontrol eder.
     *
     * @param saveClientAdvertisementRequest İlan Kaydetme Dto Nesnesi
     * @throws UndefinedClientAdvertisementTypeException Enum olduğu için öyle bir Type yoksa hata fırlatır.
     */
    @Override
    public void checkedClientAdvertisementType(SaveClientAdvertisementRequest saveClientAdvertisementRequest) {

        if (!(saveClientAdvertisementRequest.getClientAdvertisementType() == ClientAdvertisementType.PARTIAL
                || saveClientAdvertisementRequest.getClientAdvertisementType() == ClientAdvertisementType.FTL)) {

            throw new UndefinedClientAdvertisementTypeException(Translator.toLocale("lojister.clientAdvertisement.UndefinedClientAdvertisementTypeException"));
        }
    }


    /**
     * İlanın Bitip Bitmediğini Kontrol Eder.(AdvertisementStatus.FINISHED)
     *
     * @param clientAdvertisement İlan Nesnesi
     */
    @Override
    public void deleteClientAdvertisementIsFinished(ClientAdvertisement clientAdvertisement) {

        if ((clientAdvertisement.getAdvertisementStatus() == AdvertisementStatus.FINISHED)) {
            throw new ClientAdvertisementDeleteException(Translator.toLocale("lojister.clientAdvertisement.ClientAdvertisementDeleteException"));
        }
    }


    @Override
    public ClientAdvertisement findDataById(Long clientAdvertisementId) {

        Optional<ClientAdvertisement> clientAdvertisement = clientAdvertisementRepository.findById(clientAdvertisementId);

        if (clientAdvertisement.isPresent()) {
            return clientAdvertisement.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.clientAdvertisement.EntityNotFoundException"));
        }
    }

    @Override
    public ClientAdvertisement saveRepo(ClientAdvertisement clientAdvertisement) {
        return clientAdvertisementRepository.save(clientAdvertisement);
    }



    @Override
    public Long countByAdvertisementStatus(AdvertisementStatus advertisementStatus, Long clientId) {
        return clientAdvertisementRepository.countByAdvertisementStatusAndClient_Id(advertisementStatus, clientId);
    }

    @Override
    public List<ClientAdvertisement> getByProcess(Long clientId) {
        return clientAdvertisementRepository.getByProcess(clientId, Arrays.asList(
                AdvertisementProcessStatus.BID_GIVEN
                , AdvertisementProcessStatus.BID_APPROVED
                , AdvertisementProcessStatus.WAITING_WAYBILL));
    }

    @Override
    public Long countByClientIdAndCreatedDateTimeBetween(Long clientId, LocalDateTime start, LocalDateTime end) {
        return clientAdvertisementRepository.countByClient_IdAndCreatedDateTimeBetween(clientId, start, end);
    }

    @Override
    public Page<ClientAdvertisementMinimalDto> getByClientIdAndAdvertisementStatusIn(Boolean status, Pageable pageable) {
        Client currentClient = securityContextUtil.getCurrentClient();
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().isEmpty() ? Sort.by("createdDateTime").descending() : pageable.getSort());

        if (status) {
            Page<ClientAdvertisement> pageableClientAdvertisement = clientAdvertisementRepository
                    .findByClient_IdAndAdvertisementStatusIn(currentClient.getId()
                            , Arrays.asList(AdvertisementStatus.ACTIVE, AdvertisementStatus.TRANSPORT), customPageable);

            List<ClientAdvertisementMinimalDto> clientAdvertisementMinimalDtoList = clientAdvertisementMinimalDtoMapper.entityListToDtoList(pageableClientAdvertisement.getContent());
            Page<ClientAdvertisementMinimalDto> pageableDtoList = new PageImpl<>(clientAdvertisementMinimalDtoList, pageableClientAdvertisement.getPageable(), pageableClientAdvertisement.getTotalElements());

            return pageableDtoList;
        }
        if (!status) {

            List<AdvertisementStatus> advertisementStatusList = Arrays.asList(
                    AdvertisementStatus.HIDDEN
                    , AdvertisementStatus.FINISHED
                    , AdvertisementStatus.CANCEL);
            Page<ClientAdvertisement> pageableClientAdvertisement = clientAdvertisementRepository
                    .findByClient_IdAndAdvertisementStatusIn(currentClient.getId()
                            , advertisementStatusList, customPageable);
            List<ClientAdvertisementMinimalDto> clientAdvertisementMinimalDtoList = clientAdvertisementMinimalDtoMapper.entityListToDtoList(pageableClientAdvertisement.getContent());
            Page<ClientAdvertisementMinimalDto> pageableDtoList = new PageImpl<>(clientAdvertisementMinimalDtoList, pageableClientAdvertisement.getPageable(), pageableClientAdvertisement.getTotalElements());
            return pageableDtoList;
        }
        return null;
    }

    @Override
    public List<ClientAdvertisementMinimalDto> getByStartingPositionButMinimalAndHide(Double lat, Double lng) {
        return clientAdvertisementMinimalDtoMapper.customEntityListToDtoList(clientAdvertisementRepository.findByStartingAddress(lat, lng));
    }

    @Override
    public ClientAdvertisementSimpleDto getByIdButSimpleAndHide(Long id) {
        ClientAdvertisement clientAdvertisement = clientAdvertisementRepository.getByIdSimple(id);
        if (clientAdvertisement instanceof ClientAdvertisementFtl) {
            return clientAdvertisementSimpleDtoMapper.entityToFtlHideDto((ClientAdvertisementFtl) clientAdvertisement);
        }
        if (clientAdvertisement instanceof ClientAdvertisementPartial) {
            return clientAdvertisementSimpleDtoMapper.entityToPartialHideDto((ClientAdvertisementPartial) clientAdvertisement);
        }
        if (clientAdvertisement instanceof ClientAdvertisementContainer) {
            return clientAdvertisementSimpleDtoMapper.entityToContainerHideDto((ClientAdvertisementContainer) clientAdvertisement);
        }

        return null;
    }

    @Override
    public ClientAdvertisement updateClientAdvertisementProcessStatus(Long id, AdvertisementProcessStatus advertisementProcessStatus) {
        ClientAdvertisement clientAdvertisement = findDataById(id);
        clientAdvertisement.setAdvertisementProcessStatus(advertisementProcessStatus);
        return clientAdvertisementRepository.save(clientAdvertisement);

    }

}
