package com.lojister.repository.payment;

import com.lojister.model.entity.payment.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}
