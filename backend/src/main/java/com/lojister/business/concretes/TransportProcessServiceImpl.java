package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.exception.TransportProcessStatusException;
import com.lojister.core.exception.WrongTransportProcessCode;
import com.lojister.model.entity.TransportProcess;
import com.lojister.repository.transport.TransportProcessRepository;
import com.lojister.business.abstracts.TransportProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransportProcessServiceImpl implements TransportProcessService {

    private final TransportProcessRepository transportProcessRepository;

    @Value("${lojister.transport.code}")
    private String transportCodeStartsWith;



    @Override
    public TransportProcess findDataById(Long transportProcessId) {

        Optional<TransportProcess> optionalTransportProcess = transportProcessRepository.findById(transportProcessId);

        if (optionalTransportProcess.isPresent()) {

            return optionalTransportProcess.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.transportProcess.EntityNotFoundException"));
        }

    }

    @Override
    public TransportProcess findDataByTransportCode(String transportCode) {

        Optional<TransportProcess> optionalTransportProcess = transportProcessRepository.findByTransportCode(transportCode);

        if (optionalTransportProcess.isPresent()) {
            return optionalTransportProcess.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.transportProcess.EntityNotFoundException"));
        }
    }

    @Override
    public TransportProcess saveRepo(TransportProcess transportProcess) {

        return transportProcessRepository.save(transportProcess);
    }


    @Override
    public void transportCodeStartsWithCheck(String transportCode) {
        if (!transportCode.startsWith(transportCodeStartsWith)) {
            throw new WrongTransportProcessCode(Translator.toLocale("lojister.transportProcess.WrongTransportProcessCode"));
        }
    }

    @Override
    public void cargoWasDeliveredCheck(TransportProcess transportProcess) {

        if (transportProcess.getTransportProcessStatus() != TransportProcessStatus.CARGO_WAS_DELIVERED) {

            throw new TransportProcessStatusException(Translator.toLocale("lojister.transportProcess.TransportProcessStatusException.cargo"));
        }
    }

    @Override
    public void cargoOnTheWayCheck(TransportProcess transportProcess, String errorMessage,String errorMessageEn) {

        if (transportProcess.getTransportProcessStatus() != TransportProcessStatus.CARGO_ON_THE_WAY) {
            throw new TransportProcessStatusException(Translator.toLocale("lojister.transportProcess.TransportProcessStatusException.cargo"));
        }
    }

    @Override
    public void assignVehicleCheck(TransportProcess transportProcess) {

        if (transportProcess.getTransportProcessStatus() != TransportProcessStatus.ASSIGN_VEHICLE) {
            throw new TransportProcessStatusException(Translator.toLocale("lojister.transportProcess.TransportProcessStatusException.vehicle"));
        }
    }

    @Override
    public void paymentSuccessfulCheck(TransportProcess transportProcess) {

        if (transportProcess.getTransportProcessStatus() != TransportProcessStatus.PAYMENT_SUCCESSFUL) {

            throw new TransportProcessStatusException(Translator.toLocale("lojister.transportProcess.TransportProcessStatusException.customer"));
        }
    }


}
