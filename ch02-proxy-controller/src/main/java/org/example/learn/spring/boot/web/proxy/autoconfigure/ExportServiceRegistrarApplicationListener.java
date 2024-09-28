package org.example.learn.spring.boot.web.proxy.autoconfigure;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExportServiceRegistrarApplicationListener implements ApplicationListener<ApplicationContextInitializedEvent> {

    private AtomicBoolean refreshed = new AtomicBoolean(false);

    private ExportServiceRegistrar exportServiceRegistrar;

    public ExportServiceRegistrarApplicationListener(ExportServiceRegistrar exportServiceRegistrar) {
        this.exportServiceRegistrar = exportServiceRegistrar;
    }

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        if (refreshed.get()) {
            return;
        }

        refreshed.set(true);

    }
}