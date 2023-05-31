package com.lojister.repository.abroudRepository;

import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.RaitingAbroud;
import com.lojister.model.abroudModel.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt,Long> {
    @Query("SELECT u FROM Receipt u WHERE u.status = false ORDER BY u.id DESC")
    List<Receipt> findAllFalce();

}
