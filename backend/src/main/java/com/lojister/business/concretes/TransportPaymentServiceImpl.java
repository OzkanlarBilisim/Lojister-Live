package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.PayDriverDto;
import com.lojister.model.dto.TransportPaymentDetailDto;
import com.lojister.model.dto.TransportPaymentDto;
import com.lojister.model.enums.PaymentStatus;
import com.lojister.model.enums.Role;
import com.lojister.model.enums.TransportProcessType;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.TransportPaymentMapper;
import com.lojister.model.entity.TransportPayment;
import com.lojister.model.entity.TransportProcess;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.repository.transport.TransportPaymentRepository;
import com.lojister.repository.transport.TransportProcessRepository;
import com.lojister.business.abstracts.TransportPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransportPaymentServiceImpl implements TransportPaymentService {

    private final TransportPaymentRepository transportPaymentRepository;
    private final TransportProcessRepository transportProcessRepository;
    private final TransportPaymentMapper transportPaymentMapper;



    @Override
    public TransportPaymentDto getByTransportProcessId(Long transportProcessId) {

        Optional<TransportPayment> transportPayment = transportPaymentRepository.findByTransportProcess_Id(transportProcessId);

        if (transportPayment.isPresent()) {

            return transportPaymentMapper.entityToDto(transportPayment.get());

        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.transportPayment.EntityNotFoundException"));
        }
    }


    @Override
    @OnlyAdmin
    public void payTheDriver(PayDriverDto payDriverDto) {

        Optional<TransportPayment> transportPayment = transportPaymentRepository.findById(payDriverDto.getTransportPaymentId());

        if (transportPayment.isPresent()) {

            if (payDriverDto.getIsPayment()) {

                transportPayment.get().setPaymentStatus(PaymentStatus.PAYMENT_COMPLETED);
                transportPayment.get().setPaymentDate(payDriverDto.getPaymentDate());
                transportPayment.get().setPaymentTime(payDriverDto.getPaymentTime());
                transportPayment.get().setPaymentTransactionNumber(payDriverDto.getPaymentTransactionNumber());
            } else {

                transportPayment.get().setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
            }
            transportPayment.get().setPaymentDescription(payDriverDto.getPaymentDescription());
            transportPaymentRepository.save(transportPayment.get());

        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.transportPayment.EntityNotFoundException"));
        }
    }


    @Override
    @OnlyAdmin
    public List<TransportPaymentDto> getAll() {
        return transportPaymentMapper.entityListToDtoList(transportPaymentRepository.findAll());
    }

    @Override
    @OnlyAdmin
    public List<TransportPaymentDto> getPaymentWaiting() {
        return transportPaymentMapper.entityListToDtoList(transportPaymentRepository.findByPaymentStatus(PaymentStatus.PAYMENT_WAITING));
    }

    @Override
    @OnlyAdmin
    public List<TransportPaymentDto> getPaymentCompleted() {
        return transportPaymentMapper.entityListToDtoList(transportPaymentRepository.findByPaymentStatus(PaymentStatus.PAYMENT_COMPLETED));
    }

    @Override
    @OnlyAdmin
    public TransportPaymentDetailDto getTransportProcessDetailByTransportCodeForTransportPayment(String transportCode) {

        Optional<TransportProcess> transportProcess = transportProcessRepository.findByTransportCode(transportCode);

        if (!(transportProcess.isPresent())) {
            throw new EntityNotFoundException(Translator.toLocale("lojister.transportPayment.EntityNotFoundException.transportProces"));
        }

        TransportPaymentDetailDto transportPaymentDetailDto = new TransportPaymentDetailDto();


        if (transportProcess.get().getTransportProcessType() == TransportProcessType.CLIENT_TRANSPORT_PROCESS) {

            ClientTransportProcess clientTransportProcess = (ClientTransportProcess) transportProcess.get();

            transportPaymentDetailDto.setAdmissionDate(clientTransportProcess.getAdmissionDate());
           transportPaymentDetailDto.setAdmissionTime(clientTransportProcess.getAdmissionTime());
            transportPaymentDetailDto.setDeliveryDate(clientTransportProcess.getDeliveryDate());
            transportPaymentDetailDto.setDeliveryTime(clientTransportProcess.getDeliveryTime());
            transportPaymentDetailDto.setDriverFirstname(clientTransportProcess.getSummaryDriverData().getFirstName());
            transportPaymentDetailDto.setDriverLastname(clientTransportProcess.getSummaryDriverData().getLastName());
            transportPaymentDetailDto.setDriverNumber(clientTransportProcess.getSummaryDriverData().getPhone());

            if (clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getRole() == Role.ROLE_DRIVER) {

                transportPaymentDetailDto.setBossFirstname(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getFirstName());
                transportPaymentDetailDto.setBossLastname(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getLastName());
                transportPaymentDetailDto.setBossNumber(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getPhone());

            } else if (clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getRole() == Role.ROLE_DRIVER_EMPLOYEE) {

                transportPaymentDetailDto.setBossFirstname(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getBoss().getFirstName());
                transportPaymentDetailDto.setBossLastname(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getBoss().getLastName());
                transportPaymentDetailDto.setBossNumber(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getBoss().getPhone());
            }

            transportPaymentDetailDto.setCompanyName(clientTransportProcess.getVehicle().getCompany().getCommercialTitle());
            transportPaymentDetailDto.setLicencePlate(clientTransportProcess.getVehicle().getLicencePlate());
            transportPaymentDetailDto.setStartAddress(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getStartingAddress().getProvince() + ", "
                    + clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getStartingAddress().getDistrict());
            transportPaymentDetailDto.setFinishAddress(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDueAddress().getProvince() + ", "
                    + clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDueAddress().getDistrict());
            transportPaymentDetailDto.setClientFirstname(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getFirstName());
            transportPaymentDetailDto.setClientLastname(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getLastName());
            transportPaymentDetailDto.setClientNumber(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getPhone());
            transportPaymentDetailDto.setClientExplanation(clientTransportProcess.getClientExplanation());
            transportPaymentDetailDto.setStartRecipientFirstname(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getStartRecipient().getFirstName());
            transportPaymentDetailDto.setStartRecipientLastname(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getStartRecipient().getLastName());
            transportPaymentDetailDto.setStartRecipientNumber(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getStartRecipient().getPhoneNumber());
            transportPaymentDetailDto.setDueRecipientFirstname(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDueRecipient().getFirstName());
            transportPaymentDetailDto.setDueRecipientLastname(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDueRecipient().getLastName());
            transportPaymentDetailDto.setDueRecipientNumber(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDueRecipient().getPhoneNumber());
            transportPaymentDetailDto.setAdvertisementStartDate(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getCreatedDateTime());
            transportPaymentDetailDto.setBidStartDate(clientTransportProcess.getAcceptedClientAdvertisementBid().getCreatedDateTime());

            return transportPaymentDetailDto;


        } else if (transportProcess.get().getTransportProcessType() == TransportProcessType.DRIVER_TRANSPORT_PROCESS) {

            //SONRADAN EKLENECEK.
            return null;

        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.transportPayment.EntityNotFoundException.status"));
        }
    }

    @Override
    public TransportPayment saveRepo(TransportPayment transportPayment) {
        return transportPaymentRepository.save(transportPayment);
    }


}
