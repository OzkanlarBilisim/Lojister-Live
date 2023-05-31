package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.BankInformationDto;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.BankInformationMapper;
import com.lojister.model.entity.BankInformation;
import com.lojister.repository.bank.BankInformationRepository;
import com.lojister.business.abstracts.BankInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BankInformationServiceImpl implements BankInformationService {

    private final BankInformationRepository bankInformationRepository;
    private final BankInformationMapper bankInformationMapper;



    @Override
    public BankInformationDto save(BankInformationDto bankInformationDto) {

        BankInformation bankInformation = new BankInformation();
        bankInformation.setBankName(bankInformationDto.getBankName());
        bankInformation.setBranch(bankInformationDto.getBranch());
        bankInformation.setIban(bankInformationDto.getIban());
        bankInformation.setAccountNumber(bankInformationDto.getAccountNumber());
        bankInformation = bankInformationRepository.save(bankInformation);

        return bankInformationMapper.entityToDto(bankInformation);
    }

    @Override
    public BankInformationDto update(Long id, BankInformationDto bankInformationDto) {

        BankInformation bankInformation = findDataById(id);

        bankInformation.setBankName(bankInformationDto.getBankName());
        bankInformation.setBranch(bankInformationDto.getBranch());
        bankInformation.setAccountNumber(bankInformationDto.getAccountNumber());
        bankInformation.setIban(bankInformationDto.getIban());
        return bankInformationMapper.entityToDto(bankInformationRepository.save(bankInformation));
    }

    @Override
    public BankInformationDto getById(Long id) {

        BankInformation bankInformation = findDataById(id);
        return bankInformationMapper.entityToDto(bankInformation);
    }

    @Override
    public void deleteById(Long id) {
        findDataById(id);
        bankInformationRepository.deleteById(id);
    }


    //TODO SAVED BANK INFORMATION OLACAĞI İÇİN GETALL'I ORDAN ÇEKECEĞİM.
    @Override
    public List<BankInformationDto> getAll() {
        return Collections.emptyList();
    }


    @Override
    public BankInformation saveRepo(BankInformation bankInformation) {
        return bankInformationRepository.save(bankInformation);
    }


    @Override
    public BankInformation findDataById(Long id) {

        Optional<BankInformation> bankInformation = bankInformationRepository.findById(id);

        if (bankInformation.isPresent()) {

            return bankInformation.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.bankInformation.EntityNotFoundException"));
        }
    }


}
