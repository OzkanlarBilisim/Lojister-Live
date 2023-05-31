package com.lojister.business.abstracts;

import com.lojister.model.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    VerificationToken findDataByVerificationToken(String verificationToken);

    void deleteById(Long id);

    void delete(VerificationToken verificationToken);

    Boolean isExpiredToken(Long id);

    Optional<VerificationToken> findByUserIdOptional(Long userId);

    VerificationToken saveRepo(VerificationToken verificationToken);

}
