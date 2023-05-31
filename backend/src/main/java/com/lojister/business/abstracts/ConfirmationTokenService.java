package com.lojister.business.abstracts;

import com.lojister.model.entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    ConfirmationToken findById(Long id);
    ConfirmationToken save(ConfirmationToken confirmationToken);
    void delete(Long id);
    ConfirmationToken findByToken(String token);
    Optional<ConfirmationToken> findByTransportProcessId(Long transportProcessId);
}
