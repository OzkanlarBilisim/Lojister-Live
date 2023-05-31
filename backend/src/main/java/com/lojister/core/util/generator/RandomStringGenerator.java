package com.lojister.core.util.generator;

import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.repository.transport.ClientTransportProcessRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RandomStringGenerator {


    private final ClientTransportProcessRepository clientTransportProcessRepository;

    public String generateString() {

        String qrCode = RandomStringUtils.randomNumeric(8);

        Optional<ClientTransportProcess> clientTransportProcess = clientTransportProcessRepository.findByDeliverQrCodeOrReceiveQrCode(qrCode, qrCode);

        if (clientTransportProcess.isPresent()) {
            this.generateString();
        }

        return qrCode;

    }

}
