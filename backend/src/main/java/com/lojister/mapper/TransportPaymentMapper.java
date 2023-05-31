package com.lojister.mapper;

import com.lojister.model.dto.TransportPaymentDto;
import com.lojister.model.entity.TransportPayment;
import com.lojister.model.enums.TransportProcessType;
import com.lojister.model.entity.client.ClientTransportProcess;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransportPaymentMapper {


    public TransportPaymentDto entityToDto(TransportPayment transportPayment) {

        if (transportPayment == null) {
            return null;
        }

        TransportPaymentDto transportPaymentDto = new TransportPaymentDto();
        transportPaymentDto.setId(transportPayment.getId());
        transportPaymentDto.setPaymentDate(transportPayment.getPaymentDate());
        transportPaymentDto.setPaymentTime(transportPayment.getPaymentTime());
        transportPaymentDto.setTransportCode(transportPayment.getTransportProcess().getTransportCode());
        transportPaymentDto.setPaymentStatus(transportPayment.getPaymentStatus());
        transportPaymentDto.setTransportProcessType(transportPayment.getTransportProcess().getTransportProcessType());
        transportPaymentDto.setTransportProcessId(transportPayment.getTransportProcess().getId());
        transportPaymentDto.setPaymentTransactionNumber(transportPayment.getPaymentTransactionNumber());
        transportPaymentDto.setPrice(transportPayment.getPrice());
        transportPaymentDto.setPaymentDescription(transportPayment.getPaymentDescription());

        if (transportPayment.getTransportProcess().getTransportProcessType()== TransportProcessType.CLIENT_TRANSPORT_PROCESS){

            ClientTransportProcess clientTransportProcess = (ClientTransportProcess) transportPayment.getTransportProcess();

            transportPaymentDto.setBankName(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getCompany().getBankInformation().getBankName());
            transportPaymentDto.setBranch(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getCompany().getBankInformation().getBranch());
            transportPaymentDto.setAccountNumber(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getCompany().getBankInformation().getAccountNumber());
            transportPaymentDto.setIban(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getCompany().getBankInformation().getIban());

        }else if (transportPayment.getTransportProcess().getTransportProcessType()== TransportProcessType.DRIVER_TRANSPORT_PROCESS){

            //BURAYI SONRADAN YAP.

        }


        return transportPaymentDto;
    }

    public List<TransportPaymentDto> entityListToDtoList(List<TransportPayment> transportPaymentList) {

        if (transportPaymentList == null) {
            return null;
        }

        List<TransportPaymentDto> list = new ArrayList<>(transportPaymentList.size());
        for (TransportPayment transportPayment : transportPaymentList) {
            list.add(entityToDto(transportPayment));
        }
        return list;
    }

}
