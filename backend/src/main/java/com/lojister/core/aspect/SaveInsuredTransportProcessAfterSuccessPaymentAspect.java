package com.lojister.core.aspect;

import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.business.abstracts.ClientTransportProcessService;
import com.lojister.business.abstracts.InsuredTransportProcessService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class SaveInsuredTransportProcessAfterSuccessPaymentAspect {

    private final InsuredTransportProcessService insuredTransportProcessService;
    private final ClientTransportProcessService clientTransportProcessService;

    public SaveInsuredTransportProcessAfterSuccessPaymentAspect(InsuredTransportProcessService insuredTransportProcessService,
                                                                ClientTransportProcessService clientTransportProcessService) {
        this.insuredTransportProcessService = insuredTransportProcessService;
        this.clientTransportProcessService = clientTransportProcessService;
    }

    @AfterReturning(value = "@annotation(com.lojister.core.util.annotation.SaveInsuredTransportProcessAfterSuccessPayment)", returning = "returnValue")
    public void saveInsuredTransportProcessAfterSuccessPayment(JoinPoint joinPoint, Object returnValue) {

        try {
            String status = (String) returnValue;
            String transportCode = (String) joinPoint.getArgs()[0];

            if (status.equals("payment_success")) {
                ClientTransportProcess clientTransportProcess = clientTransportProcessService.findDataByTransportCode(transportCode);
                insuredTransportProcessService.save(clientTransportProcess);
            }
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
        }

    }

}
