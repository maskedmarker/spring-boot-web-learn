package org.example.learn.spring.boot.web.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloWebMvcApplication {

    private static final Logger logger = LoggerFactory.getLogger(HelloWebMvcApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HelloWebMvcApplication.class);
        logger.info("main thread is ending");
    }
}
