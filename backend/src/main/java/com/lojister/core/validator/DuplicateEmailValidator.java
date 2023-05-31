package com.lojister.core.validator;

import com.lojister.core.exception.ValidatorException;
import com.lojister.core.exception.validator.DuplicateMailException;
import com.lojister.core.i18n.Translator;
import com.lojister.model.entity.User;
import com.lojister.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DuplicateEmailValidator implements EmailValidator<String> {

   private final UserRepository userRepository;

    @Override
    public void validate(String email) throws ValidatorException {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new DuplicateMailException(Translator.toLocale("lojister.account.DuplicateMailException"));
        }
    }

}
