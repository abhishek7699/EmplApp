package com.EmplApp.EmplApp.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceMonitorAspect {

    private static final Logger LOGGER= LoggerFactory.getLogger(PerformanceMonitorAspect.class);


    @Around("execution(* com.EmplApp.EmplApp..*(..))")
    public Object  timeCalculate(ProceedingJoinPoint jp) throws Throwable {
        Long start=System.currentTimeMillis();

        Object obj=jp.proceed();

         Long end=System.currentTimeMillis();

        LOGGER.info("the time taken for "+ jp.getSignature().getName()+" to get executed "+(end-start));

        return obj;

    }

}
