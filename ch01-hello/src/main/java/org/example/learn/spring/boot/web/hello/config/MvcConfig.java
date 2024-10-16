package org.example.learn.spring.boot.web.hello.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Spring Boot提供了一个内置的HttpServletRequest日志记录解决方案(需要将其日志级别调整为debug级别,才能生效)
 */
@Configuration
public class MvcConfig {

    private static final Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(true);
        filter.setIncludePayload(true);
        filter.setIncludeClientInfo(true);

        return filter;
    }
}
