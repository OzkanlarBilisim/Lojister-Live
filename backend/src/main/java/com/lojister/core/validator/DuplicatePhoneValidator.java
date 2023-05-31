package com.lojister.core.validator;

import com.lojister.core.exception.ValidatorException;
import com.lojister.core.exception.validator.DuplicatePhoneException;
import com.lojister.model.entity.User;
import com.lojister.repository.account.UserRepository;
import com.lojister.core.validator.formatter.PhoneFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DuplicatePhoneValidator implements PhoneValidator<String> {

    private final UserRepository userRepository;

    private final PhoneFormatter phoneFormatter;


    @Override
    public void validate(String phone) throws ValidatorException {

        String formattedNumber = phoneFormatter.format(phone);

        Optional<User> user = userRepository.findByPhone(formattedNumber);

        if (user.isPresent()) {
            throw new DuplicatePhoneException("Bu Numara Daha Önceden Alınmıştır.");
        }


    }
}
