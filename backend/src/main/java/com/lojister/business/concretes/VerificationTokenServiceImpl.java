package com.lojister.business.concretes;

import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.i18n.Translator;
import com.lojister.model.entity.VerificationToken;
import com.lojister.repository.account.VerificationTokenRepository;
import com.lojister.business.abstracts.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;





    @Override
    public VerificationToken findDataByVerificationToken(String verificationToken) {

        Optional<VerificationToken> verificationTokenData = verificationTokenRepository.findByVerificationToken(verificationToken);

        if (verificationTokenData.isPresent()) {

            return verificationTokenData.get();

        } else {

            throw new EntityNotFoundException(Translator.toLocale("lojister.verificationToken.EntityNotFoundException"));
        }
    }


    @Override
    public void deleteById(Long id) {

        Optional<VerificationToken> verificationToken = verificationTokenRepository.findById(id);

        if (verificationToken.isPresent()) {

            verificationTokenRepository.deleteById(id);

        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.verificationToken.EntityNotFoundException.wrog"));
        }
    }

    @Override
    public void delete(VerificationToken verificationToken) {

        verificationTokenRepository.deleteById(verificationToken.getId());

    }

    @Override
    public Boolean isExpiredToken(Long tokenId) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        Optional<VerificationToken> verificationToken = verificationTokenRepository.findById(tokenId);

        if (verificationToken.isPresent()) {

            LocalDateTime controlDateTime = verificationToken.get().getVerificationCreatedDateTime();

            controlDateTime = controlDateTime.plusDays(3);

            if (controlDateTime.isAfter(currentDateTime)) {

                return false;
            } else {

                return true;
            }

        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.verificationToken.EntityNotFoundException"));
        }
    }

    @Override
    public Optional<VerificationToken> findByUserIdOptional(Long userId) {
        return verificationTokenRepository.findByUser_Id(userId);
    }

    @Override
    public VerificationToken saveRepo(VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

}
