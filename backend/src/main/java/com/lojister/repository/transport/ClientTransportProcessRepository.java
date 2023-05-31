package com.lojister.repository.transport;

import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.enums.BidStatus;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.entity.client.ClientTransportProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientTransportProcessRepository extends JpaRepository<ClientTransportProcess, Long> {


    Page<ClientTransportProcess> findByVehicle_Driver_IdAndAcceptedClientAdvertisementBid_BidStatusIn(Long driverId,List<BidStatus> bidStatusList,Pageable pageable);

    List<ClientTransportProcess> findByVehicle_Driver_IdOrderByIdDesc(Long driverId);

    List<ClientTransportProcess> findByVehicle_IdAndTransportProcessStatus(Long vehicleId, TransportProcessStatus transportProcessStatus);

    @Query(value = "select ctp from #{#entityName} ctp " +
            "left join fetch ctp.acceptedClientAdvertisementBid acb " +
            "left join fetch ctp.vehicle " +
            "left join fetch acb.clientAdvertisement ca " +
            "left join fetch ca.client " +
            "left join fetch acb.driverBidder " +
            "where ctp.transportCode = :transportCode"
    )
    Optional<ClientTransportProcess> findByTransportCode(@Param(value = "transportCode") String transportCode);

  /*  @Query(value = "select ctp from #{#entityName} ctp " +
            "left join fetch ctp.acceptedClientAdvertisementBid acb " +
            "where acb.id = :id"
    )*/
    ClientTransportProcess findByAcceptedClientAdvertisementBid_Id( Long id);

    Optional<ClientTransportProcess> findByReceiveQrCode(String receiveQrCode);

    Optional<ClientTransportProcess> findByDeliverQrCode(String deliverQrCode);

    Optional<ClientTransportProcess> findByDeliverQrCodeOrReceiveQrCode(String deliverQrCode, String receiveQrCode);

    @Query(value = "select distinct ctp " +
            "from " + "ClientTransportProcess ctp " + "join ctp.acceptedClientAdvertisementBid cab " + "join cab.clientAdvertisement ca " +
            "where " + "ca.id=:clientAdvertisementId")
    ClientTransportProcess findClientTransportProcessByClientAdvertisement_Id(@Param(value = "clientAdvertisementId") Long clientAdvertisementId);


    @Query(value = "select distinct ctp " +
            "from " + "ClientTransportProcess ctp " + "join ctp.acceptedClientAdvertisementBid cab " + "join cab.clientAdvertisement ca " +
            "join ca.client clt " + "where " + "clt.id=:clientId")
    List<ClientTransportProcess> findClientTransportProcessByClient_Id(@Param(value = "clientId") Long clientId);


    @Query(value = "select distinct ctp " +
            "from " + "ClientTransportProcess ctp " + "join ctp.acceptedClientAdvertisementBid cab " + "join cab.driverBidder d " +
            "where " + "d.id=:driverId")
    List<ClientTransportProcess> findClientTransportProcessByDriver_Id(@Param(value = "driverId") Long driverId);


    @Query(value = "select distinct ctp from #{#entityName} ctp " +
            "left join fetch ctp.acceptedClientAdvertisementBid cab " +
            "left join fetch cab.driverBidder d "+
            "left join fetch cab.clientAdvertisement ca " +
            "left join fetch ca.startingAddress left join fetch ca.dueAddress left join fetch ca.cargoTypes " +
            "where (d.id = :driverId) and (ctp.transportProcessStatus in :transportProcessStatusList) "

    )
    List<ClientTransportProcess> findByDriverIdAndTransportProcessStatus(@Param(value = "driverId") Long driverId, @Param(value = "transportProcessStatusList") List<TransportProcessStatus> transportProcessStatusList);

   /*@Query(value = "select distinct count(ctp) from #{#entityName} ctp " +
           " where (ctp.acceptedClientAdvertisementBid.driverBidder.id=:driverId) and (acceptedClientAdvertisementBid.bidStatus in :bidStatusList)")
    Long countByDriver_IdAndInBidStatus(@Param(value = "driverId") Long driverId, @Param(value = "bidStatusList")List<BidStatus> bidStatusList);*/


    Long countByAcceptedClientAdvertisementBid_DriverBidder_IdAndCreatedDateTimeBetween(Long driverId, LocalDateTime start, LocalDateTime end);

}
