package com.lojister.core.aspect;


import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.business.abstracts.ClientTransportProcessService;
import com.lojister.business.abstracts.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class SendMailAfterPaymentToInsuranceCompanyAspect {

    private final ClientTransportProcessService clientTransportProcessService;
    private final SendMailService sendMailService;

    public SendMailAfterPaymentToInsuranceCompanyAspect(ClientTransportProcessService clientTransportProcessService,
                                                        SendMailService sendMailService) {
        this.clientTransportProcessService = clientTransportProcessService;
        this.sendMailService = sendMailService;
    }

    @AfterReturning(value = "@annotation(com.lojister.core.util.annotation.SendMailAfterPaymentToInsuranceCompany)", returning = "returnValue")
    public void sendMailAfterPaymentToInsuranceCompany(JoinPoint joinPoint, Object returnValue) {

        try {
            String status = (String) returnValue;
            String transportCode = (String) joinPoint.getArgs()[0];

            if (status.equals("payment_success")) {
                ClientTransportProcess clientTransportProcess = clientTransportProcessService.findDataByTransportCode(transportCode);
                sendMailService.sendMailAfterPaymentToInsuranceCompany(clientTransportProcess);

            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }

    }

}
