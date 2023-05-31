package com.lojister.repository.bank;

import com.lojister.model.entity.BankInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankInformationRepository extends JpaRepository<BankInformation,Long> {

}
