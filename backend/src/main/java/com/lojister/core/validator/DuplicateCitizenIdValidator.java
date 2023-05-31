package com.lojister.core.validator;

import com.lojister.core.exception.ValidatorException;
import com.lojister.core.exception.validator.DuplicateCitizenIdException;
import com.lojister.core.i18n.Translator;
import com.lojister.core.validator.base.Validator;
import com.lojister.repository.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DuplicateCitizenIdValidator implements Validator<String> {

    private final ClientRepository clientRepository;

    @Override
    public void validate(String citizenId) throws ValidatorException {
       if (clientRepository.findByCitizenIdAndCitizenIdNotNull(citizenId).isPresent()) {
           throw new DuplicateCitizenIdException(Translator.toLocale("lojister.account.DuplicateCitizenIdExceptio"));
       }
    }
}
