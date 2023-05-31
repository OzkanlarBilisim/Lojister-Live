package com.lojister.repository.abroudRepository;

import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.RaitingAbroud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaitingAbroudRepository extends JpaRepository<RaitingAbroud,Integer> {
    @Query("SELECT u FROM RaitingAbroud u WHERE u.user_Driver.id = :DriverId ORDER BY u.id DESC")
    List<RaitingAbroud> findByDriverId(@Param(value = "DriverId") long DriverId);
}