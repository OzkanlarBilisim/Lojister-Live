package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessDto;
import com.lojister.model.enums.DocumentUploadStatus;
import lombok.Data;

@Data
public class InsuredTransportProcessResponseDto extends BaseDto {

    private ClientTransportProcessDto clientTransportProcess;

    private DocumentUploadStatus documentUploadStatus;

}
