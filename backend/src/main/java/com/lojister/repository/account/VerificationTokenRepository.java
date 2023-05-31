package com.lojister.repository.account;


import com.lojister.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    Optional<VerificationToken> findByVerificationToken(String verificationToken);

    Optional<VerificationToken> findByUser_Id(Long id);

}
