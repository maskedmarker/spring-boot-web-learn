package org.example.learn.spring.boot.web.proxy;

import org.example.learn.spring.boot.web.proxy.autoconfigure.EnableExportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableExportService(basePackages = "org.example.learn.spring.boot.web.proxy.facade")
@SpringBootApplication
public class ExportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExportServiceApplication.class);
    }
}
