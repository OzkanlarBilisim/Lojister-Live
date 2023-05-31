package com.lojister.repository.abroudRepository;

import com.lojister.model.abroudModel.ShipsmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipsmentInfoRepository extends JpaRepository<ShipsmentInfo,Integer> {

    @Query("SELECT u FROM ShipsmentInfo u WHERE u.adAbroud.id = :advertID")
    ShipsmentInfo getTransportInfo(@Param(value = "advertID") int advertID);

}
