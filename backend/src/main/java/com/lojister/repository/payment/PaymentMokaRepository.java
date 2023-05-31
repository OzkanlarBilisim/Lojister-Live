package com.lojister.repository.payment;

import com.lojister.model.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMokaRepository extends JpaRepository<Payment, Long> {
}