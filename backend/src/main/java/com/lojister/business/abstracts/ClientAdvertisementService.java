package com.lojister.business.abstracts;

import com.lojister.controller.advertisement.FilterClientAdvertisementRequest;
import com.lojister.controller.advertisement.SaveAdvertisementExcelRequest;
import com.lojister.controller.advertisement.SaveClientAdvertisementRequest;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementMinimalDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementSimpleDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.service.osrm.model.LatLng;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientAdvertisementService {

    void checkedClientAdvertisementSaveDatesOperation(SaveClientAdvertisementRequest saveClientAdvertisementRequest);

    ClientAdvertisementDto save(SaveClientAdvertisementRequest dto);

    ClientAdvertisementDto getById(Long id);

    void deleteById(Long id);

    ClientAdvertisementDto activateAdvertisement(Long clientAdvertisementId);

    ClientAdvertisementDto hideAdvertisement(Long clientAdvertisementId);

    List<ClientAdvertisementDto> getAll();

    List<ClientAdvertisementSimpleDto> getAllButSimple();

    List<ClientAdvertisementMinimalDto> getAdvertisementFilterAndSort(FilterClientAdvertisementRequest filterClientAdvertisementRequest, String sort);

    List<ClientAdvertisementDto> getFinishedAdvertisement();

    List<ClientAdvertisementDto> getHiddenAdvertisement();

    Long getHiddenAdvertisementCount();

    Long getFinishedAdvertisementCount();

    List<ClientAdvertisementDto> getActiveAdvertisement();

    List<ClientAdvertisementDto> getTransportAdvertisement();

    List<ClientAdvertisementMinimalDto> getCargoOnTheWayAdvertisement(FilterClientAdvertisementRequest filterClientAdvertisementRequest, String sort);

    List<ClientAdvertisementDto> getMyAdvertisement();

    List<ClientAdvertisementDto> getMyAdvertisementByProvincesAndDateRange(String startProvince, String finishProvince, String startDate, String endDate);

    List<ClientAdvertisementDto> getPartialAdvertisement();

    List<ClientAdvertisementDto> getFTLAdvertisement();

    List<ClientAdvertisementDto> getByVehicleTypeIdList(List<Long> vehicleTypeIdList);

    List<ClientAdvertisementDto> getByTrailerTypeIdList(List<Long> trailerTypeIdList);

    List<ClientAdvertisementDto> getByTrailerFloorTypeIdList(List<Long> trailerFloorTypeIdList);

    List<ClientAdvertisementDto> getByTrailerFeatureIdList(List<Long> trailerFeatureIdList);

    List<ClientAdvertisementDto> getByProvincesAndDateRange(String startDate, String endDate, String startProvince, String dueProvince);

    Long getActiveAdvertisementCount();

    Long getMyActiveAdvertisementCount();

    List<ClientAdvertisementBidDto> getClientAdvertisementBidsByClientAdvertisementId(Long clientAdvertisementId);

    List<SaveAdvertisementExcelRequest> uploadFile(FileUploadUtil.FileResult result);

    void checkedClientAdvertisementSaveTimesOperation(SaveClientAdvertisementRequest saveClientAdvertisementRequest);

    void checkedClientAdvertisementType(SaveClientAdvertisementRequest saveClientAdvertisementRequest);

    void deleteClientAdvertisementIsFinished(ClientAdvertisement clientAdvertisement);

    ClientAdvertisement findDataById(Long clientAdvertisementId);

    ClientAdvertisement saveRepo(ClientAdvertisement clientAdvertisement);


    Long countByAdvertisementStatus(AdvertisementStatus advertisementStatus,Long clientId);
    List<ClientAdvertisement> getByProcess(Long clientId);
    Long countByClientIdAndCreatedDateTimeBetween(Long clientId, LocalDateTime start,LocalDateTime end);
    Page<ClientAdvertisementMinimalDto> getByClientIdAndAdvertisementStatusIn(Boolean status, Pageable pageable);
    List<ClientAdvertisementMinimalDto> getByStartingPositionButMinimalAndHide(Double lat,Double lng );
    ClientAdvertisementSimpleDto getByIdButSimpleAndHide(Long id);
    ClientAdvertisement updateClientAdvertisementProcessStatus(Long id , AdvertisementProcessStatus advertisementProcessStatus);


}
