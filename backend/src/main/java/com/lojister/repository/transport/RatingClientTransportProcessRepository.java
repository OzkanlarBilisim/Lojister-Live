package com.lojister.repository.transport;

import com.lojister.model.entity.RatingClientTransportProcess;
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
public interface RatingClientTransportProcessRepository extends JpaRepository<RatingClientTransportProcess, Long> {

    @Query(value = "select distinct rctp " + "from " + "RatingClientTransportProcess rctp " + "join  rctp.clientTransportProcess ctp " + "join ctp.acceptedClientAdvertisementBid cb " +
            "join  cb.clientAdvertisement ca " + "join ca.client cl " + "where " + "cl.id= :clientId " + "and " + "ctp.id= :clientTransportProcessId")
    Optional<RatingClientTransportProcess> findByClientTransportProcess_IdAndClient_Id(@Param(value = "clientTransportProcessId") Long clientTransportProcessId, @Param(value = "clientId") Long clientId);

    @Query(value = "select distinct rctp " + "from " + "RatingClientTransportProcess rctp " + "join rctp.clientTransportProcess ctp " + "join ctp.acceptedClientAdvertisementBid cb " +
            "join cb.driverBidder db " + "join db.company co " + "where " + "co.id= :companyId ")
    List<RatingClientTransportProcess> findRatingByCompanyId(@Param(value = "companyId") Long companyId, Sort id);

    @Query(value = "select count(rctp) " + "from " + "RatingClientTransportProcess rctp " + "join rctp.clientTransportProcess ctp " + "join ctp.acceptedClientAdvertisementBid cab " +
            "join cab.driverBidder db " + "join db.company co " + "where " + "co.id= :companyId " + "and " + "rctp.rating= :rating")
    Long ratingCountByCompany_IdAndRatingScore(@Param(value = "companyId") Long companyId, @Param(value = "rating") Long rating);

    @Query(value = "select distinct rctp from RatingClientTransportProcess rctp " +
            "join rctp.clientTransportProcess ctp " +
            "join ctp.acceptedClientAdvertisementBid cab " +
            "join cab.driverBidder db " +
            "join db.company co " +
            "where co.id= :companyId and rctp.rating in :ratingList")
    Page<RatingClientTransportProcess> findByCompany_IdAndRatingList(@Param(value = "companyId") Long companyId, @Param(value = "ratingList") List<Long> ratingList, Pageable pageable);

}
