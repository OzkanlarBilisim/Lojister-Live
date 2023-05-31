package com.lojister.core.aspect;

import com.lojister.model.enums.Role;
import com.lojister.core.exception.UnauthorizedTransactionException;
import com.lojister.model.entity.driver.Driver;
import com.lojister.core.util.SecurityContextUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class DriverBossAspect {

    @Autowired
    SecurityContextUtil securityContextUtil;

    @Before("@annotation(com.lojister.core.util.annotation.DriverBoss)")
    public void driverIsActive(JoinPoint joinPoint) {

        Driver currentDriver = securityContextUtil.getCurrentDriver();

        if (currentDriver.getRole() != Role.ROLE_DRIVER) {
            throw new UnauthorizedTransactionException("Bu İşlemi Sadece Şirket Sahibi Yapabilir.");
        }

    }

}
