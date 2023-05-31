package com.lojister.repository.abroudRepository;


import com.lojister.model.abroudModel.DocumentsAbroud;
import com.lojister.model.abroudModel.PaymentControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentControlRepository extends JpaRepository<PaymentControl, Long> {
    @Query("SELECT u FROM PaymentControl u WHERE u.BankKey = :key")
    PaymentControl keyFind(@Param(value = "key") String key);
}