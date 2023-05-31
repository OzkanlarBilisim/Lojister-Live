package com.lojister.core.aspect;


import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)"+
            " || within(@org.springframework.stereotype.Controller *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }


    @Pointcut("within(com.lojister.repository.*)" +
            " || within(com.lojister.service.*)" +
            " || within(com.lojister.controller.*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }


    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("");
        System.out.println("------------------------------------------------");
        System.out.println(e.getLocalizedMessage());
        System.out.println(e.getClass());
        System.out.println(e.getStackTrace()[0].getLineNumber());
        System.out.println(joinPoint.getSignature());
        System.out.println(joinPoint.getSignature().getName());
        System.out.println(joinPoint.getSignature().getDeclaringTypeName());
        System.out.println(e.getCause() != null ? e.getCause() : "CAUSE NULL");
        System.out.println("------------------------------------------------");
        System.out.println("");

        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "CAUSE NULL");
    }


    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
       // if (log.isDebugEnabled()) {
            System.out.println(joinPoint.getSignature());
            log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    //    }
        try {
            Object result = joinPoint.proceed();
        //    if (log.isDebugEnabled()) {
                System.out.println( joinPoint.getSignature());
                log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
    //        }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }


}


