package org.example.learn.spring.boot.web.proxy.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * spring-boot-starter-data-jpa依赖spring-boot-starter-aop
 *
 * 被@Aspect注解的类需要再添加@Configuration/@Component,才能被spring容器管理
 */
//@Configuration
@Aspect
public class AopConfig {

    private static final Logger logger = LoggerFactory.getLogger(AopConfig.class);

    @Pointcut("within(org.example.learn.spring.boot.web.hello.controller..*)")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public void logController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logInvocation(proceedingJoinPoint);
    }

    @Pointcut("within(org.example.learn.spring.boot.web.hello.service..*)")
    public void servicePointcut() {}

    @Around("servicePointcut()")
    public void logService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logInvocation(proceedingJoinPoint);
    }

    private Object logInvocation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnValue = null;
        try {
            // before logging
            logger.info("before invocation of {}. param={}", proceedingJoinPoint.getTarget(), proceedingJoinPoint.getArgs());
            returnValue = proceedingJoinPoint.proceed();
            logger.info("after invocation of {}. returnValue={}", proceedingJoinPoint.getTarget(), returnValue);
            return returnValue;
        } catch (Throwable e) {
            logger.info("exception on invocation of {}. returnValue={}", proceedingJoinPoint.getTarget(), returnValue);
            throw e;
        }
    }
}
