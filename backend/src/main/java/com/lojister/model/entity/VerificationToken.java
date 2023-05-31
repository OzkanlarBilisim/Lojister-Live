package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken extends AbstractTimestampEntity {

    private String verificationToken;

    private LocalDateTime verificationCreatedDateTime;

    @OneToOne
    @JoinColumn(nullable = false,unique = true)
    private User user;

    public VerificationToken(User user) {
        this.verificationToken = UUID.randomUUID().toString();
        this.verificationCreatedDateTime = LocalDateTime.now();
        this.user = user;

    }

}
