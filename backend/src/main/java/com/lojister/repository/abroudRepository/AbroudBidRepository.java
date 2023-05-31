package com.lojister.repository.abroudRepository;

import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.abroudBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AbroudBidRepository extends JpaRepository<abroudBid,Integer> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE abroudBid u SET u.companyName = :name WHERE u.id = :id")
    int updateUserName(@Param("id") Long id, @Param("name") String name);
    @Query("SELECT u FROM abroudBid u WHERE u.companyId = :companyId AND u.adAbroud.id = :id ORDER BY u.id DESC")
    List<abroudBid> findClientAbroud(@Param(value = "companyId") int companyId, @Param(value = "id") int id);
    @Query("SELECT u FROM abroudBid u WHERE u.adAbroud.id = :id ORDER BY u.id DESC")
    List<abroudBid> getAllAdwertismantId(@Param(value = "id") int id);
    @Query("SELECT u FROM abroudBid u WHERE u.adAbroud.id = :id AND u.status = 'APPROVED'")
    List<abroudBid> getBidApproved(@Param(value = "id") int id);
    @Query("SELECT u FROM abroudBid u WHERE u.adAbroud.id = :id AND u.status = 'APPROVED'")
    List<abroudBid> findbidAndabraud(@Param(value = "id") int id);

    @Query("SELECT u FROM abroudBid u WHERE u.adAbroud.id = :id AND u.status = 'PAYMENT_SUCCESSFUL' OR u.adAbroud.id = :id AND u.status = 'APPROVED'")
    List<abroudBid> findbidPaymentSuccessAproved(@Param(value = "id") int id);

    @Query("SELECT u FROM abroudBid u WHERE u.companyId = :driverId ORDER BY u.id DESC")
    List<abroudBid> driverBidList(@Param(value = "driverId") int driverId);

    @Query("SELECT u FROM abroudBid u WHERE u.id = :bidID")
    List<abroudBid> bidAndAdvertRequestDto(@Param(value = "bidID") int bidID);

    @Query("SELECT u FROM abroudBid u WHERE u.adAbroud.id = :bidID AND u.companyId = :companyID")
    List<abroudBid> bidAndAdvertRequest2Dto(@Param(value = "bidID") int bidID, @Param(value = "companyID") int companyID);

    @Query("SELECT u FROM abroudBid u WHERE u.id = :id")
    List<abroudBid>IdFind(@Param(value = "id") int id);

}
