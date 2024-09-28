package org.example.learn.spring.boot.web.proxy.config;

import org.example.learn.spring.boot.web.proxy.autoconfigure.ExportServiceRegistrar;
import org.example.learn.spring.boot.web.proxy.autoconfigure.ExportServiceRegistrarApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring-boot-starter-data-jpa依赖spring-boot-starter-aop
 *
 * 被@Aspect注解的类需要再添加@Configuration/@Component,才能被spring容器管理
 */
//@Configuration
public class ExportServiceConfig {

    @Bean
    public ExportServiceRegistrar exportServiceRegistrar() {
        return new ExportServiceRegistrar();
    }

    @Bean
    public ExportServiceRegistrarApplicationListener exportServiceRegistrarApplicationListener(ExportServiceRegistrar exportServiceRegistrar) {
        return new ExportServiceRegistrarApplicationListener(exportServiceRegistrar);
    }
}
