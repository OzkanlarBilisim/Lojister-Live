package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.client.ClientTransportProcess;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class ConfirmationToken extends AbstractTimestampEntity {

    private LocalDateTime expiration;
    private String email;
    private String token;

    @OneToOne
    @JoinColumn
    private ClientTransportProcess clientTransportProcess;

    public static ConfirmationToken create(ClientTransportProcess clientTransportProcess) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(UUID.randomUUID().toString());
        confirmationToken.setExpiration(LocalDateTime.now().plusDays(3));
        confirmationToken.setEmail(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getEmail());
        confirmationToken.setClientTransportProcess(clientTransportProcess);
        return confirmationToken;
    }
}
