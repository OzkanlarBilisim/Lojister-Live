package com.lojister.repository.transport;

import com.lojister.model.enums.PaymentStatus;
import com.lojister.model.entity.TransportPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportPaymentRepository extends JpaRepository<TransportPayment,Long> {

    Optional<TransportPayment> findByTransportProcess_Id(Long transportProcessId);

    List<TransportPayment> findByPaymentStatus(PaymentStatus paymentStatus);

}
