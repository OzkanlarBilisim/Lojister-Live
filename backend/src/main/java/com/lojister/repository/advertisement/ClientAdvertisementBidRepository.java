package com.lojister.repository.advertisement;

import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.BidStatus;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.model.enums.TransportProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ClientAdvertisementBidRepository extends JpaRepository<ClientAdvertisementBid,Long> {

    @Query(value = "select  cab from #{#entityName} cab " +
            "left join fetch cab.summaryAdvertisementData " +
            "left join fetch cab.driverBidder db " +
            "left join fetch cab.clientAdvertisement  " +
            "where (db.id = :driverId) " +
            "and (cab.bidStatus in :bidStatusList)"
    ,countQuery = "select count(cab) from #{#entityName} cab " +
            "where (cab.driverBidder.id = :driverId) " +
            "and (cab.bidStatus in :bidStatusList)"
    )
    Page<ClientAdvertisementBid> findByDriverBidder_Id(@Param(value = "driverId") Long driverId,@Param(value = "bidStatusList") List<BidStatus> bidStatusList, Pageable pageable);

    @Query("select count(cab) from #{#entityName} cab " +
            "where (cab.driverBidder.id = :driverId) " +
            "and (cab.bidStatus in :bidStatusList)")
    Long countByDriver_IdAndInBidStatus(@Param(value = "driverId") Long driverId, @Param(value = "bidStatusList")List<BidStatus> bidStatusList);

    List<ClientAdvertisementBid> findByClientAdvertisement_Id(Long id);

    @Query(value = "select  cab from #{#entityName} cab " +
            "left join fetch cab.clientAdvertisement ca " +
            "left join fetch ca.clientTransportProcess ctp " +
            "left join fetch cab.summaryAdvertisementData " +
            "left join fetch cab.driverBidder db " +
            "left join fetch ca.simpleStartingAddress ssa " +
            "left join fetch ca.simpleDueAddress sda " +
            "left join fetch ca.startingAddress " +
            "left join fetch ca.dueAddress " +
            "left join fetch ca.cargoTypes " +
            "where (db.id = :driverId) " +
            "and (ctp.transportProcessStatus in :transportProcessStatusList) " +
            "and (cab.bidStatus in :bidStatusList) " +
            "and ((ssa.province like %:startingAddressProvince%) " +
            "and (ssa.district like %:startingAddressDistrict) " +
            "and (ssa.neighborhood like %:startingAddressNeighborhood)) " +
            "and ((sda.province like %:dueAddressProvince%) " +
            "and (sda.district like %:dueAddressDistrict) " +
            "and (sda.neighborhood like %:dueAddressNeighborhood)) " +
            "and (ca.clientAdvertisementType in :clientAdvertisementTypeList)"

    )
    List<ClientAdvertisementBid> findByDriverCargoOnTheWay(
            @Param(value = "driverId") Long driverId,
            @Param(value = "transportProcessStatusList") List<TransportProcessStatus> transportProcessStatusList,
            @Param(value = "startingAddressProvince") String startingAddressProvince,
            @Param(value = "startingAddressDistrict") String startingAddressDistrict,
            @Param(value = "startingAddressNeighborhood") String startingAddressNeighborhood,
            @Param(value = "dueAddressProvince") String dueAddressProvince,
            @Param(value = "dueAddressDistrict") String dueAddressDistrict,
            @Param(value = "dueAddressNeighborhood") String dueAddressNeighborhood,
            @Param(value = "clientAdvertisementTypeList") List<ClientAdvertisementType> clientAdvertisementTypeList,
            @Param(value= "bidStatusList") List<BidStatus> bidStatusList,
            Sort sort
    );
    List<ClientAdvertisementBid> findByDriverBidder_IdAndClientAdvertisement_AdvertisementProcessStatusIn(Long driverId,List<AdvertisementProcessStatus> advertisementProcessStatusList);

    Page<ClientAdvertisementBid> findByClientAdvertisement_Id(Long id,Pageable pageable);

    List<ClientAdvertisementBid> findByClientAdvertisement_IdAndDriverBidder_Id(Long clientAdvertisementId, Long driverId);

    @Query(value = "select count(cab) " + "from "+ "ClientAdvertisementBid cab " + "where cab.clientAdvertisement.client.id=:clientId")
    Long countByClientAdvertisement_Client_Id(@Param(value = "clientId") Long clientId);

    Long countClientAdvertisementBidByDriverBidder_Id(Long driverId);

    Optional<ClientAdvertisementBid> findByClientAdvertisement_IdAndDriverBidder_IdAndBidStatus(Long clientAdvertisementId, Long driverId, BidStatus bidStatus);

    @Query("SELECT u FROM ClientAdvertisementBid u WHERE u.clientAdvertisement.id = :advertID AND u.bidStatus = 'COMPLETED'")
    Optional<ClientAdvertisementBid> findSelectBidAdvertId(@Param(value = "advertID") Long advertID);

}
