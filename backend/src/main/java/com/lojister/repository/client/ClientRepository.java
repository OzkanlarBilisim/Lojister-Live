package com.lojister.repository.client;

import com.lojister.model.entity.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    Client findByPhone(String phone);

    Client findByEmail(String email);

    Optional<Client> findByCitizenIdAndCitizenIdNotNull(String citizenId);
    List<Client> findByCompany_Id(Long id);

    Page<Client> findByBoss_Id(Long bossId, Pageable pageable);
}
