package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.CurrencyUnitDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.CurrencyUnitMapper;
import com.lojister.model.entity.adminpanel.CurrencyUnit;
import com.lojister.repository.currencyunit.CurrencyUnitRepository;
import com.lojister.business.abstracts.dynamic.CurrencyUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CurrencyUnitServiceImpl implements CurrencyUnitService {

    private final CurrencyUnitRepository currencyUnitRepository;
    private final CurrencyUnitMapper currencyUnitMapper;


    @Autowired
    public CurrencyUnitServiceImpl(CurrencyUnitRepository currencyUnitRepository,
                                   CurrencyUnitMapper currencyUnitMapper) {
        this.currencyUnitRepository = currencyUnitRepository;
        this.currencyUnitMapper = currencyUnitMapper;
    }


    @Override
    public CurrencyUnit findDataById(Long id) {

        Optional<CurrencyUnit> currencyUnit = currencyUnitRepository.findById(id);

        if (currencyUnit.isPresent()) {

            return currencyUnit.get();
        } else {

            throw new EntityNotFoundException("Para Birimi Bulunamadi");
        }
    }

    @Override
    public CurrencyUnit findCurrencyUnitByCurrencyAbbreviation(String currencyAbbreviation) {

        Optional<CurrencyUnit> currencyUnit = currencyUnitRepository.findByCurrencyAbbreviation(currencyAbbreviation);

        if (currencyUnit.isPresent()) {

            return currencyUnit.get();

        } else {

            throw new EntityNotFoundException("Para Birimi Bulunamadi");
        }
    }

    @Override
    public void duplicateCurrencyNameCheck(String currencyName) {
        Optional<CurrencyUnit> currencyUnit = currencyUnitRepository.findByCurrencyNameIgnoreCase(currencyName);
        if (currencyUnit.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Para Birimi Adı Zaten Mevcuttur.");
        }
    }

    @Override
    public void duplicateCurrencyAbbreviationCheck(String currencyAbbreviation) {
        Optional<CurrencyUnit> currencyUnit = currencyUnitRepository.findByCurrencyAbbreviationIgnoreCase(currencyAbbreviation);
        if (currencyUnit.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Para Biimi Kısaltması Zaten Mevcuttur.");
        }
    }


    @Override
    @OnlyAdmin
    public CurrencyUnitDto save(CurrencyUnitDto currencyUnitDto) {

        duplicateCurrencyNameCheck(currencyUnitDto.getCurrencyName());
        duplicateCurrencyAbbreviationCheck(currencyUnitDto.getCurrencyAbbreviation());

        CurrencyUnit currencyUnit = new CurrencyUnit();
        currencyUnit.setCurrencyName(currencyUnitDto.getCurrencyName());
        currencyUnit.setCurrencyAbbreviation(currencyUnitDto.getCurrencyAbbreviation());
        currencyUnit.setCurrencySymbol(currencyUnitDto.getCurrencySymbol());
        currencyUnit.setDynamicStatus(DynamicStatus.ACTIVE);
        currencyUnit = currencyUnitRepository.save(currencyUnit);

        return currencyUnitMapper.entityToDto(currencyUnit);
    }

    @Override
    public CurrencyUnitDto getById(Long id) {

        CurrencyUnit currencyUnit = findDataById(id);
        return currencyUnitMapper.entityToDto(currencyUnit);
    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);
        currencyUnitRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<CurrencyUnitDto> getAll() {
        return currencyUnitMapper.entityListToDtoList(currencyUnitRepository.findAll());
    }


    @Override
    @OnlyAdmin
    public void activate(Long id) {

        CurrencyUnit currencyUnit = findDataById(id);

        currencyUnit.setDynamicStatus(DynamicStatus.ACTIVE);
        currencyUnitRepository.save(currencyUnit);

    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        CurrencyUnit currencyUnit = findDataById(id);

        currencyUnit.setDynamicStatus(DynamicStatus.PASSIVE);
        currencyUnitRepository.save(currencyUnit);

    }

    @Override
    public List<CurrencyUnitDto> getActive() {
        return currencyUnitMapper.entityListToDtoList(currencyUnitRepository.findByDynamicStatus(DynamicStatus.ACTIVE));
    }

    @Override
    public List<CurrencyUnitDto> getPassive() {
        return currencyUnitMapper.entityListToDtoList(currencyUnitRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }

}
