package com.lojister.repository.advertisement;

import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.model.enums.RegionAdvertisementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientAdvertisementRepository extends JpaRepository<ClientAdvertisement, Long>, JpaSpecificationExecutor<ClientAdvertisement> {

    List<ClientAdvertisement> findByAdvertisementStatusOrderByAdStartingDate(AdvertisementStatus advertisementStatus);

    List<ClientAdvertisement> findByAdvertisementStatusOrderByIdDesc(AdvertisementStatus advertisementStatus);

    List<ClientAdvertisement> findByClient_IdOrderByIdDesc(Long id);

    List<ClientAdvertisement> findByClientAdvertisementType(ClientAdvertisementType advertisementType);

    Long countByAdvertisementStatus(AdvertisementStatus advertisementStatus);

    Long countByAdvertisementStatusAndClient_Id(AdvertisementStatus advertisementStatus, Long clientId);

    //-----------------------------------------------------------

    @Query("select distinct ca from #{#entityName} ca left join fetch ca.vehicleTypes left join fetch ca.loadType left join fetch ca.simpleStartingAddress left join fetch ca.simpleDueAddress left join fetch ca.startingAddress left join fetch ca.dueAddress left join fetch ca.cargoTypes left join fetch ca.clientTransportProcess")
    List<ClientAdvertisement> getAllSimple();

    @Query("select ca from #{#entityName} ca " +
            "left join fetch ca.vehicleTypes " +
            "left join fetch ca.loadType " +
            "left join fetch ca.simpleStartingAddress " +
            "left join fetch ca.simpleDueAddress " +
            "left join fetch ca.startingAddress " +
            "left join fetch ca.dueAddress " +
            "left join fetch ca.cargoTypes " +
            "left join fetch ca.clientTransportProcess " +
            "left join fetch ca.packagingType " +
            "left join fetch ca.currencyUnit " +
            "where ca.id = :id")
    ClientAdvertisement getByIdSimple(@Param(value = "id") Long id);

    @Query("select distinct ca from #{#entityName} ca left join fetch ca.simpleStartingAddress  left join fetch ca.simpleDueAddress left join fetch ca.startingAddress left join fetch ca.dueAddress left join fetch ca.cargoTypes left join fetch ca.clientTransportProcess where (ca.advertisementStatus in :advertisementStatusList)")
    List<ClientAdvertisement> getAllMinimal(@Param(value = "advertisementStatusList") List<AdvertisementStatus> advertisementStatusList);

    @Query("select distinct ca from #{#entityName} ca " +
            "left join fetch ca.simpleStartingAddress ssa " +
            "left join fetch ca.simpleDueAddress sda " +
            "left join fetch ca.startingAddress " +
            "left join fetch ca.dueAddress " +
            "left join fetch ca.cargoTypes " +
            "left join fetch ca.clientTransportProcess " +
            "where (ca.advertisementStatus in :advertisementStatusList) " +
            "and ((ssa.province like %:startingAddressProvince%) " +
            "and (ssa.district like %:startingAddressDistrict) " +
            "and (ssa.neighborhood like %:startingAddressNeighborhood)) " +
            "and ((sda.province like %:dueAddressProvince%) " +
            "and (sda.district like %:dueAddressDistrict) " +
            "and (sda.neighborhood like %:dueAddressNeighborhood)) " +
            "and (ca.clientAdvertisementType in :clientAdvertisementTypeList) "+
            "and (ca.regionAdvertisementType in :regionAdvertisementTypeList)")
    List<ClientAdvertisement> getAdvertisementFilterAndSort(@Param(value = "advertisementStatusList") List<AdvertisementStatus> advertisementStatusList,
                                                            @Param(value = "startingAddressProvince") String startingAddressProvince,
                                                            @Param(value = "startingAddressDistrict") String startingAddressDistrict,
                                                            @Param(value = "startingAddressNeighborhood") String startingAddressNeighborhood,
                                                            @Param(value = "dueAddressProvince") String dueAddressProvince,
                                                            @Param(value = "dueAddressDistrict") String dueAddressDistrict,
                                                            @Param(value = "dueAddressNeighborhood") String dueAddressNeighborhood,
                                                            @Param(value = "clientAdvertisementTypeList") List<ClientAdvertisementType> clientAdvertisementTypeList,
                                                            @Param(value = "regionAdvertisementTypeList") List<RegionAdvertisementType> regionAdvertisementTypeList,
                                                            Sort sort
    );

    @Query("select distinct ca from #{#entityName} ca " +
            "left join fetch ca.simpleStartingAddress ssa " +
            "left join fetch ca.client c " +
            "left join fetch ca.simpleDueAddress sda " +
            "left join fetch ca.startingAddress " +
            "left join fetch ca.dueAddress " +
            "left join fetch ca.cargoTypes " +
            "left join fetch ca.clientTransportProcess " +
            "where  c.id=:clientId " +
            "and (ca.advertisementProcessStatus in :advertisementProcessStatusList) " +
            "and ((ssa.province like %:startingAddressProvince%) " +
            "and (ssa.district like %:startingAddressDistrict) " +
            "and (ssa.neighborhood like %:startingAddressNeighborhood)) " +
            "and ((sda.province like %:dueAddressProvince%) " +
            "and (sda.district like %:dueAddressDistrict) " +
            "and (sda.neighborhood like %:dueAddressNeighborhood)) " +
            "and (ca.clientAdvertisementType in :clientAdvertisementTypeList)")
    List<ClientAdvertisement> getCargoOnTheWayAdvertisement(
            @Param(value = "clientId") Long clientId,
            @Param(value = "advertisementProcessStatusList") List<AdvertisementProcessStatus> advertisementProcessStatusList,
            @Param(value = "startingAddressProvince") String startingAddressProvince,
            @Param(value = "startingAddressDistrict") String startingAddressDistrict,
            @Param(value = "startingAddressNeighborhood") String startingAddressNeighborhood,
            @Param(value = "dueAddressProvince") String dueAddressProvince,
            @Param(value = "dueAddressDistrict") String dueAddressDistrict,
            @Param(value = "dueAddressNeighborhood") String dueAddressNeighborhood,
            @Param(value = "clientAdvertisementTypeList") List<ClientAdvertisementType> clientAdvertisementTypeList,
            Sort sort
    );

    @Query(value = "select distinct ca " + "from " + "#{#entityName} ca " + "where " + "(((ca.adStartingDate between :startDate and :endDate) or (ca.adDueDate between :startDate and :endDate)) and (ca.startingAddress.province=:startProvince) and (ca.advertisementStatus=:advertisementStatus)) " + "order by ca.adStartingDate")
    List<ClientAdvertisement> findByStartingAddress_ProvinceAndDateRange(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, @Param(value = "startProvince") String startProvince, @Param(value = "advertisementStatus") AdvertisementStatus advertisementStatus);

    @Query(value = "select distinct ca " + "from " + "#{#entityName} ca " + "where " + "(((ca.adStartingDate between :startDate and :endDate) or (ca.adDueDate between :startDate and :endDate)) and (ca.dueAddress.province=:dueProvince) and (ca.advertisementStatus=:advertisementStatus)) " + "order by ca.adStartingDate")
    List<ClientAdvertisement> findByDueAddress_ProvinceAndDateRange(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, @Param(value = "dueProvince") String dueProvince, @Param(value = "advertisementStatus") AdvertisementStatus advertisementStatus);

    @Query(value = "select distinct ca " + "from " + "#{#entityName} ca " + "where " + "(((ca.adStartingDate between :startDate and :endDate) or (ca.adDueDate between :startDate and :endDate)) and (ca.startingAddress.province=:startProvince and ca.dueAddress.province=:dueProvince) and (ca.advertisementStatus=:advertisementStatus)) " + "order by ca.adStartingDate")
    List<ClientAdvertisement> findByDateRangeAndProvince(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, @Param(value = "startProvince") String startProvince, @Param(value = "dueProvince") String dueProvince, @Param(value = "advertisementStatus") AdvertisementStatus advertisementStatus);

    @Query(value = "select distinct ca " + "from " + "#{#entityName} ca " + "where " + "(((ca.adStartingDate between :startDate and :endDate) or (ca.adDueDate between :startDate and :endDate)) and (ca.advertisementStatus=:advertisementStatus)) " + "order by ca.adStartingDate")
    List<ClientAdvertisement> findByDateRange(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, @Param(value = "advertisementStatus") AdvertisementStatus advertisementStatus);


    //----------------------------------------------------------- HISTORY START ---------------------

    @Query(value = "select distinct ca " + "from " + "#{#entityName} ca " + "where " + "(((ca.adStartingDate between :startDate and :endDate) or (ca.adDueDate between :startDate and :endDate)) and (ca.startingAddress.province = :startProvince) and (ca.dueAddress.province = :dueProvince ) and (ca.client.id=:clientId) and (ca.advertisementStatus=:advertisementStatus)) " + "order by ca.adStartingDate")
    List<ClientAdvertisement> findMyAdvertisementByDateRangeAndProvince(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, @Param(value = "startProvince") String startProvince, @Param(value = "dueProvince") String dueProvince, @Param(value = "clientId") Long clientId, @Param(value = "advertisementStatus") AdvertisementStatus advertisementStatus);


    @Query(value = "select distinct ca " + "from " + "#{#entityName} ca " + "where " + "(((ca.adStartingDate between :startDate and :endDate) or (ca.adDueDate between :startDate and :endDate)) and (ca.startingAddress.province=:startProvince) and (ca.client.id=:clientId) and (ca.advertisementStatus=:advertisementStatus)) " + "order by ca.adStartingDate")
    List<ClientAdvertisement> findMyAdvertisementByStartingAddress_ProvinceAndDateRange(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, @Param(value = "startProvince") String startProvince, @Param(value = "clientId") Long clientId, @Param(value = "advertisementStatus") AdvertisementStatus advertisementStatus);


    @Query(value = "select distinct ca " + "from " + "#{#entityName} ca " + "where " + "(((ca.adStartingDate between :startDate and :endDate) or (ca.adDueDate between :startDate and :endDate)) and (ca.dueAddress.province=:dueProvince) and (ca.client.id=:clientId) and (ca.advertisementStatus=:advertisementStatus)) " + "order by ca.adStartingDate")
    List<ClientAdvertisement> findMyAdvertisementByDueAddress_ProvinceAndDateRange(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, @Param(value = "dueProvince") String dueProvince, @Param(value = "clientId") Long clientId, @Param(value = "advertisementStatus") AdvertisementStatus advertisementStatus);


    @Query(value = "select distinct ca " + "from " + "#{#entityName} ca " + "where " + "(((ca.adStartingDate between :startDate and :endDate) or (ca.adDueDate between :startDate and :endDate))  and (ca.client.id=:clientId) and (ca.advertisementStatus=:advertisementStatus)) " + "order by ca.adStartingDate")
    List<ClientAdvertisement> findMyAdvertisementByDateRange(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, @Param(value = "clientId") Long clientId, @Param(value = "advertisementStatus") AdvertisementStatus advertisementStatus);


    //------------------------------------------------------------HISTORY END-----------------------------


    @Query(value = "select distinct ca " +
            "from " + "#{#entityName} ca " + "join  ca.vehicleTypes vt " + "where " + "vt.id in :vehicleTypeIdList")
    List<ClientAdvertisement> findByVehicleTypeIdList(@Param(value = "vehicleTypeIdList") List<Long> vehicleTypeIdList);

    @Query(value = "select distinct ca " +
            "from " + "#{#entityName} ca " + "join  ca.trailerTypes tt " + "where " + "tt.id in :trailerTypeIdList")
    List<ClientAdvertisement> findByTrailerTypeIdList(@Param(value = "trailerTypeIdList") List<Long> trailerTypeIdList);

    @Query(value = "select distinct ca " +
            "from " + "#{#entityName} ca " + "join  ca.trailerFloorTypes tft " + "where " + "tft.id in :trailerFloorTypeIdList")
    List<ClientAdvertisement> findByTrailerFloorTypeIdList(@Param(value = "trailerFloorTypeIdList") List<Long> trailerFloorTypeIdList);

    @Query(value = "select distinct ca " +
            "from " + "#{#entityName} ca " + "join ca.trailerFeatures tf " + "where " + "tf.id in :trailerFeatureIdList")
    List<ClientAdvertisement> findByTrailerFeatureIdList(@Param(value = "trailerFeatureIdList") List<Long> trailerFeatureIdList);


    @Query(value = "select distinct  ca from #{#entityName} ca left join fetch ca.simpleStartingAddress left join fetch ca.simpleDueAddress left join fetch ca.startingAddress left join fetch ca.dueAddress left join fetch ca.cargoTypes left join fetch ca.clientTransportProcess ctp where (ca.client.id=:clientId) and (ca.advertisementProcessStatus in :advertisementProcessStatusList)")
    List<ClientAdvertisement> getByProcess(@Param(value = "clientId") Long clientId, @Param(value = "advertisementProcessStatusList") List<AdvertisementProcessStatus> advertisementProcessStatusList);

    Long countByClient_IdAndCreatedDateTimeBetween(Long clientId, LocalDateTime start, LocalDateTime end);


    @Query(value = "select distinct ca from #{#entityName} ca  left join fetch ca.startingAddress left join fetch ca.dueAddress left join fetch ca.cargoTypes left join fetch ca.clientTransportProcess ctp where (ca.client.id=:clientId) and (ca.advertisementStatus in :advertisementStatusList) "
            , countQuery = "select count(ca) from #{#entityName} ca  where (ca.client.id=:clientId) and (ca.advertisementStatus in :advertisementStatusList)"
    )
    Page<ClientAdvertisement> findByClient_IdAndAdvertisementStatusIn(@Param(value = "clientId") Long clientId, @Param(value = "advertisementStatusList") List<AdvertisementStatus> advertisementStatusList, Pageable pageable);

    Optional<ClientAdvertisement> findFirstByOrderByIdDesc();

    @Query(value = "select distinct ca from #{#entityName} ca left join fetch ca.vehicleTypes left join fetch ca.loadType left join fetch ca.simpleStartingAddress ssa left join fetch ca.simpleDueAddress left join fetch ca.startingAddress left join fetch ca.dueAddress left join fetch ca.cargoTypes left join fetch ca.clientTransportProcess where ((ssa.lat= :lat)  and  (ssa.lng= :lng)) ")
    List<ClientAdvertisement> findByStartingAddress(@Param(value = "lat") Double lat, @Param(value = "lng") Double lng);

    @Query("SELECT u FROM ClientAdvertisement u WHERE u.id = :id")
    List<ClientAdvertisement>IdFind(@Param(value = "id") int id);
}
