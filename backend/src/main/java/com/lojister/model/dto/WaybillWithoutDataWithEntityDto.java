package com.lojister.model.dto;


import com.lojister.model.enums.WaybillStatus;
import com.lojister.model.entity.client.ClientTransportProcess;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WaybillWithoutDataWithEntityDto {

    private Long id;

    private ClientTransportProcess clientTransportProcess;

    private WaybillStatus waybillStatus;


    public WaybillWithoutDataWithEntityDto(Long id, ClientTransportProcess clientTransportProcess, WaybillStatus waybillStatus) {
        this.id = id;
        this.clientTransportProcess = clientTransportProcess;
        this.waybillStatus = waybillStatus;
    }

}
