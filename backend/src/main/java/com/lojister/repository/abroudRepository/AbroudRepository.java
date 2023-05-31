package com.lojister.repository.abroudRepository;

import com.lojister.model.abroudModel.AdAbroud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbroudRepository extends JpaRepository<AdAbroud,Integer> {
    public List<AdAbroud> findByOrderByIdDesc();

    @Query("SELECT u FROM AdAbroud u WHERE u.id = :id")
    List<AdAbroud>IdFind(@Param(value = "id") int id);

    @Query("SELECT u FROM AdAbroud u WHERE u.advertisementStatus = 'WAITING' ORDER BY u.id DESC")
    List<AdAbroud> findAllActiveUsers();

     @Query("SELECT u FROM AdAbroud u WHERE u.client_id = :clientID ORDER BY u.id DESC")
     List<AdAbroud> findClientAbroud(@Param(value = "clientID") String clientID);

     @Query("SELECT u FROM AdAbroud u WHERE u.advertisementStatus = 'TEMPORARY_METHOD' ORDER BY u.id DESC")
     List<AdAbroud> getTEMPORARY_METHOD();

}
