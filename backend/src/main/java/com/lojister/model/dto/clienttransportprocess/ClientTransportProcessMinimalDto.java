package com.lojister.model.dto.clienttransportprocess;

import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.enums.TransportProcessType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientTransportProcessMinimalDto {

    private Long id;
    private TransportProcessStatus transportProcessStatus;
    private TransportProcessType transportProcessType;
    private String transportCode;

}
