package com.example.gym.aspects;

import com.example.gym.utils.TransactionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {}

    @Pointcut("execution(* com.example..*Service.*(..))")
    public void serviceMethods() {}

    @Before("restController()")
    public void beforeRestCall(JoinPoint joinPoint) {
        String transactionId = TransactionContext.generateTransactionId();
        logger.info("Transaction ID [{}] - Starting REST call: {}", transactionId, joinPoint.getSignature());
    }

    @AfterReturning(value = "restController()", returning = "response")
    public void afterRestCall(JoinPoint joinPoint, ResponseEntity<?> response) {

        String transactionId = TransactionContext.getTransactionId();
        logger.info("Transaction ID [{}] - Completed REST call: {}, Response: {}",
                transactionId, joinPoint.getSignature(), response);
    }

    @AfterThrowing(value = "restController()", throwing = "exception")
    public void afterRestCallError(JoinPoint joinPoint, Exception exception) {
        String transactionId = TransactionContext.getTransactionId();
        logger.error("Transaction ID [{}] - Error during REST call: {}, Exception: {}",
                transactionId, joinPoint.getSignature(), exception.getMessage());
    }

    @Before("serviceMethods()")
    public void beforeServiceCall(JoinPoint joinPoint) {
        String transactionId = TransactionContext.getTransactionId();
        logger.info("Transaction ID [{}] - Invoking service method: {}", transactionId, joinPoint.getSignature());
    }

    @After("serviceMethods()")
    public void afterServiceCall(JoinPoint joinPoint) {
        String transactionId = TransactionContext.getTransactionId();
        logger.info("Transaction ID [{}] - Completed service method: {}", transactionId, joinPoint.getSignature());
    }
}

