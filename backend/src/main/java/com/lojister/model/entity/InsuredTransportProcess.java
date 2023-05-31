package com.lojister.model.entity;

import com.lojister.model.enums.DocumentUploadStatus;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.client.ClientTransportProcess;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor
public class InsuredTransportProcess extends AbstractTimestampEntity {

    @OneToOne
    private ClientTransportProcess clientTransportProcess;

    @Enumerated(EnumType.STRING)
    private DocumentUploadStatus documentUploadStatus;

    public static InsuredTransportProcess create(ClientTransportProcess clientTransportProcess) {
        InsuredTransportProcess insuredTransportProcess = new InsuredTransportProcess();
        insuredTransportProcess.clientTransportProcess = clientTransportProcess;
        insuredTransportProcess.documentUploadStatus = DocumentUploadStatus.WAITING;
        return insuredTransportProcess;
    }

    public InsuredTransportProcess documentUploadSuccess() {
        this.documentUploadStatus = DocumentUploadStatus.SUCCESSFUL;
        return this;
    }

    public InsuredTransportProcess documentUploadSetStatusWaiting() {
        this.documentUploadStatus = DocumentUploadStatus.WAITING;
        return this;
    }

}
