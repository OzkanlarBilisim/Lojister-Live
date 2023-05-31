package com.lojister.repository.transport;

import com.lojister.model.entity.TransportProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportProcessRepository extends JpaRepository<TransportProcess,Long> {

    Optional<TransportProcess> findByTransportCode(String transportCode);

    Optional<TransportProcess> findFirstByTransportCodeContainsOrderByIdDesc(String transportCode);

}
