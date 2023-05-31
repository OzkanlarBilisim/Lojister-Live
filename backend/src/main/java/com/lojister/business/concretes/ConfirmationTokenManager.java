package com.lojister.business.concretes;

import com.lojister.business.abstracts.ConfirmationTokenService;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.model.entity.ConfirmationToken;
import com.lojister.repository.transport.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmationTokenManager implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public ConfirmationToken findById(Long id) {
        return confirmationTokenRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public void delete(Long id) {
        confirmationTokenRepository.deleteById(id);
    }

    @Override
    public ConfirmationToken findByToken(String token) {
       return confirmationTokenRepository.findByToken(token).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<ConfirmationToken> findByTransportProcessId(Long transportProcessId) {
        return confirmationTokenRepository.findByClientTransportProcess_Id(transportProcessId);
    }


}
