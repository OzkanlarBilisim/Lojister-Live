package com.lojister.business.abstracts;

import com.lojister.controller.advertisement.FilterClientAdvertisementRequest;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidMinimalDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidSaveDto;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.BidStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ClientAdvertisementBidService {

    ClientAdvertisementBidDto save(ClientAdvertisementBidSaveDto clientAdvertisementBidDto, Long clientAdvertisementId);

    ClientAdvertisementBidDto getById(Long id);

    void deleteById(Long id);

    List<ClientAdvertisementBidDto> getAll();

    Page<ClientAdvertisementBidDto> getAdvertisementBidsByAdvertisementId(Long advertisementId, Pageable pageable);

    List<ClientAdvertisementBidDto> getAdvertisementBidsByCargoOnTheWay(FilterClientAdvertisementRequest filterClientAdvertisementRequest, String sort);

    Page<ClientAdvertisementBidMinimalDto> getMyBids(Boolean status, Pageable pageable);

    void updateBidStatus(Boolean value, Long advertisementBidId);

    Long myAdvertisementBidsCount();

 //   void timeoutBidCheck(List<ClientAdvertisementBid> clientAdvertisementBidList);

    Long myBidsCount();


    List<ClientAdvertisementBidDto> getDriverIdAndAdvertisementProcessStatusIn(Long driverId, List<AdvertisementProcessStatus> advertisementProcessStatusList);

    void checkedClientAdvertisementBidSaveDatesOperation(ClientAdvertisement clientAdvertisement);

    void correctDriverCheck(ClientAdvertisementBid clientAdvertisementBid);

    void inWaitingStatusClientAdvertisementBidIsExist(Long clientAdvertisementId, Long currentDriverId);

    void delete(ClientAdvertisementBid clientAdvertisementBid);

    ClientAdvertisementBid saveRepo(ClientAdvertisementBid clientAdvertisementBid);
    Long countByDriverIdAndInBidStatus(List<BidStatus> bidStatusList);
    ClientAdvertisementBid findDataById(Long clientAdvertisementBidId);

    List<ClientAdvertisementBid> findAllByClientAdvertisementIdRepo(Long clientAdvertisementId);

    List<ClientAdvertisementBid> findByClientAdvertisementIdAndDriverBidderId(Long clientAdvertisementId, Long driverBidderId);

    void onlyOneBidCheck(ClientAdvertisementBid clientAdvertisementBid);

    void correctClientCheck(ClientAdvertisement clientAdvertisement);

    List<ClientAdvertisementBid> changeTheStatusOfAllBidToClosed(ClientAdvertisementBid acceptedClientAdvertisementBid);

    List<ClientAdvertisementBid> checkAdvertisementExpiration(List<ClientAdvertisementBid> clientAdvertisementBidList);
}
