package com.lojister.model.dto;

import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessDto;
import com.lojister.model.enums.WaybillStatus;
import lombok.Data;

@Data
public class WaybillWithoutDataDto {

    private Long id;

    private ClientTransportProcessDto clientTransportProcess;

    private WaybillStatus waybillStatus;

}
