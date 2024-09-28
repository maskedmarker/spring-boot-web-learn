package org.example.learn.spring.boot.web.proxy.autoconfigure;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExportServiceRegistrarApplicationListener implements ApplicationListener<ApplicationContextInitializedEvent> {

    private AtomicBoolean refreshed = new AtomicBoolean(false);

    private ExportServiceRegistrar exportServiceRegistrar;

    private AnnotationConfigServletWebServerApplicationContext applicationContext;

    public ExportServiceRegistrarApplicationListener(ExportServiceRegistrar exportServiceRegistrar) {
        this.exportServiceRegistrar = exportServiceRegistrar;
    }

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        if (refreshed.get()) {
            return;
        }
        refreshed.set(true);

        if (event.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext) {
            this.applicationContext = (AnnotationConfigServletWebServerApplicationContext) event.getApplicationContext();
        }

        BeanDefinitionRegistry beanDefinitionRegistry = applicationContext.getDefaultListableBeanFactory();

        // todo
    }
}