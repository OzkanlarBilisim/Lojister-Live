package com.lojister.repository.company;

import com.lojister.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    Optional<Company> findByTaxAdministrationAndTaxNumber(String taxAdministration, String taxNumber);

    List<Company> findByCommercialTitleContainsOrderByIdDesc(String commercialTitle);

}
