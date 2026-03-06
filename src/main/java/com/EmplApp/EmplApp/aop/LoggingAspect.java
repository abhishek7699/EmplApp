package com.EmplApp.EmplApp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {


    private static final Logger LOGGER= LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.EmplApp.EmplApp..*(..))")
    public void logMethodCall(JoinPoint jp){
        LOGGER.info("Method Called "+ jp.getSignature().getName());
    }

    @After("execution(* com.EmplApp.EmplApp..*(..))")
    public void logMethodSuccess(JoinPoint jp){
        LOGGER.info("Method Executed "+ jp.getSignature().getName());
    }
    @AfterThrowing("execution(* com.EmplApp.EmplApp..*(..))")
    public void logMethodThrow(JoinPoint jp){
        LOGGER.info("Method Thrown "+ jp.getSignature().getName());
    }


    @AfterReturning("execution(* com.EmplApp.EmplApp..*(..))")
    public void logMethodReturned(JoinPoint jp){
        LOGGER.info("Method Returned "+ jp.getSignature().getName());
    }


}
