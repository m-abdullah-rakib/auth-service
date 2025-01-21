package com.dp.authservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Logging method execution
    @Before("execution(* com.dp.authservice..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) {
            logger.info("Method {} with args: {} is executing", joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
        }
    }

    //Logging returned value
    @AfterReturning(pointcut = "execution(* com.dp.authservice..*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (logger.isInfoEnabled()) {
            logger.info("Method {} finished and returned value: {}", joinPoint.getSignature().toShortString(), result);
        }
    }

    //Logging thrown exception
    @AfterThrowing(pointcut = "execution(* com.dp.authservice.service..*.*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        if (logger.isErrorEnabled()) {
            logger.error("Exception thrown at {}. Exception message: {}", joinPoint.getSignature().toShortString(), exception.getMessage(), exception);
        }
    }

    //Logging method execution time
    @Around("execution(* com.dp.authservice..*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        if (logger.isInfoEnabled()) {
            logger.info("Method {} execution time: {}ms", joinPoint.getSignature().toShortString(), executionTime);
        }
        return result;
    }
}