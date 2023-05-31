package com.lojister.core.aspect;

import com.lojister.core.exception.UnApprovedAccountException;
import com.lojister.model.entity.client.Client;
import com.lojister.core.util.SecurityContextUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConfirmedClientAspect {

    @Autowired
    SecurityContextUtil securityContextUtil;

    @Before("@annotation(com.lojister.core.util.annotation.ConfirmedClient)")
    public void clientIsActive(JoinPoint joinPoint) {

        Client currentClient = securityContextUtil.getCurrentClient();

        if (!(currentClient.getMailConfirmed() || currentClient.getPhoneConfirmed())) {

            throw new UnApprovedAccountException("Hesap Onaylanmamistir.");
        }

    }

}
