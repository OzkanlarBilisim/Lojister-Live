package com.lojister.core.validator;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.UpdateUserPhoneCheckDto;
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
public class UpdateOperationDuplicatePhoneValidator implements PhoneValidator<UpdateUserPhoneCheckDto> {

    private final UserRepository userRepository;

    private final PhoneFormatter phoneFormatter;

    @Override
    public void validate(UpdateUserPhoneCheckDto updateUserPhoneCheckDto) throws ValidatorException {

        String formatterPhone = phoneFormatter.format(updateUserPhoneCheckDto.getPhone());

        Optional<User> userDb = userRepository.findByPhone(formatterPhone);

        if (userDb.isPresent()) {

            if (!(updateUserPhoneCheckDto.getUser().getPhone().equals(updateUserPhoneCheckDto.getPhone()))) {
                throw new DuplicatePhoneException(updateUserPhoneCheckDto.getPhone() + Translator.toLocale("lojister.account.DuplicatePhoneException"));
            }
        }


    }
}
