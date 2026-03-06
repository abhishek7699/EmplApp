package com.EmplApp.EmplApp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RequestParameterValidation {

    @Around("execution(* com.EmplApp.EmplApp.controller.EmpControllerQuery.listAll(int ,int ))")
    public Object validate(ProceedingJoinPoint jp) throws Throwable {
        int page= (int)jp.getArgs()[0];
        int size=(int)jp.getArgs()[1];

        if(page>=0 && size >0 && size <30){
            return jp.proceed();
        }
        else{
            throw new NullPointerException("invalid Page and size");
        }

    }

}
