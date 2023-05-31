package com.lojister.core.validator;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.UpdateUserEmailCheckDto;
import com.lojister.core.exception.ValidatorException;
import com.lojister.core.exception.validator.DuplicateMailException;
import com.lojister.model.entity.User;
import com.lojister.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UpdateOperationDuplicateEmailValidator implements EmailValidator<UpdateUserEmailCheckDto> {


   private final UserRepository userRepository;

    @Override
    public void validate(UpdateUserEmailCheckDto updateUserEmailCheckDto) throws ValidatorException {

        Optional<User> userDb = userRepository.findByEmail(updateUserEmailCheckDto.getEmail());

        if (userDb.isPresent()) {

            if (!(updateUserEmailCheckDto.getUser().getEmail().equals(updateUserEmailCheckDto.getEmail()))) {
                throw new DuplicateMailException(updateUserEmailCheckDto.getEmail() + Translator.toLocale("lojister.account.DuplicateMailException"));
            }
        }

    }
}