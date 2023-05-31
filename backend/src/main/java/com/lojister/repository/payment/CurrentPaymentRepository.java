package com.lojister.repository.payment;

import com.lojister.model.entity.payment.CurrentPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrentPaymentRepository extends JpaRepository<CurrentPayment, Long> {
    @Query("SELECT u FROM CurrentPayment u WHERE u.user.id = :userID ")
    List<CurrentPayment> findClient(@Param(value = "userID") Long userID);
}