package com.lojister.repository.payment;

import com.lojister.model.entity.payment.AfterPay;
import com.lojister.model.entity.payment.RegisteredCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AfterPayRepository extends JpaRepository<AfterPay, Long> {

    Optional<AfterPay>findAfterPayByAdAbroud_Id(int adAbroud_id);
}
