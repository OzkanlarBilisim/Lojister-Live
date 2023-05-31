package com.lojister.repository.abroudRepository;

import com.lojister.model.abroudModel.DocumentsAbroud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

@Repository
public interface AbroudDocumentsRepository extends JpaRepository<DocumentsAbroud,Integer> {
    @Query("SELECT u FROM DocumentsAbroud u WHERE u.wanting = :wanting AND u.transportId = :transportID ")
    Optional<DocumentsAbroud> findWantingAndTransportID(@Param(value = "wanting") String wanting, @Param(value = "transportID") int transportID);

}