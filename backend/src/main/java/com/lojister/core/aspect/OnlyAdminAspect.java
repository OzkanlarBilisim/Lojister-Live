package com.lojister.core.aspect;

import com.lojister.core.exception.InvalidRoleException;
import com.lojister.model.enums.Role;
import com.lojister.core.util.SecurityContextUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OnlyAdminAspect {

    @Autowired
    SecurityContextUtil securityContextUtil;

    @Before("@annotation(com.lojister.core.util.annotation.OnlyAdmin)")
    public void onlyAdmin(JoinPoint joinPoint) {

        Role currentUserRole = securityContextUtil.getCurrentUserRole();

        if (currentUserRole!=Role.ROLE_ADMIN) {

            throw new InvalidRoleException("Sadece Admin İşlem Yapabilir.");

        }

    }

}
