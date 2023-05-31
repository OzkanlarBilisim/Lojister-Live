package com.lojister.repository.currencyunit;

import com.lojister.model.entity.adminpanel.CurrencyUnit;
import com.lojister.model.enums.DynamicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyUnitRepository extends JpaRepository<CurrencyUnit, Long> {

    List<CurrencyUnit> findByDynamicStatus(DynamicStatus dynamicStatus);

    Optional<CurrencyUnit> findByCurrencyNameIgnoreCase(String currencyName);

    Optional<CurrencyUnit> findByCurrencyAbbreviationIgnoreCase(String currencyAbbreviation);

    Optional<CurrencyUnit> findByCurrencyAbbreviation(String currencyAbbreviation);


}





