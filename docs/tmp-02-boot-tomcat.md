# spring-boot-web关于tomcat的内部实现

## TomcatWebServer

### TomcatServletWebServerFactory

TomcatServletWebServerFactory是关于tomcat的WebServerFactory实现类
```text
public WebServer getWebServer(ServletContextInitializer... initializers) {
    if (this.disableMBeanRegistry) {
        Registry.disableRegistry();
    }
    Tomcat tomcat = new Tomcat();
    File baseDir = (this.baseDirectory != null) ? this.baseDirectory : createTempDir("tomcat");
    tomcat.setBaseDir(baseDir.getAbsolutePath());
    Connector connector = new Connector(this.protocol);
    connector.setThrowOnFailure(true);
    tomcat.getService().addConnector(connector);
    customizeConnector(connector);
    tomcat.setConnector(connector);
    tomcat.getHost().setAutoDeploy(false);
    configureEngine(tomcat.getEngine());
    for (Connector additionalConnector : this.additionalTomcatConnectors) {
        tomcat.getService().addConnector(additionalConnector);
    }
    // 关于tomcat的用户定制化逻辑
    prepareContext(tomcat.getHost(), initializers);
    return getTomcatWebServer(tomcat);
}

protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
    return new TomcatWebServer(tomcat, getPort() >= 0, getShutdown());
}
```
### TomcatWebServer

如果spring容器还未初始化完成,此时WebServer就开始接收网络请求,那么网络请求就无法得到合理的处理.
如何让WebServer对象实例化后根据需要,适时地接收网络请求?
这里的适时指的应该是spring容器完成初始化后.
答案就是看情况,根据具体的实现类来完成hack.

TomcatWebServer对象在创建后,此时tomcat已经初始化完成.
此时TomcatWebServer对象将tomcat的Service的Connector移除了,导致无法tomcat无法接收网络请求;
在WebServer.start方法被调用时,再将Connector归还.

```text
public TomcatWebServer(Tomcat tomcat, boolean autoStart, Shutdown shutdown) {
    Assert.notNull(tomcat, "Tomcat Server must not be null");
    this.tomcat = tomcat;
    this.autoStart = autoStart;
    this.gracefulShutdown = (shutdown == Shutdown.GRACEFUL) ? new GracefulShutdown(tomcat) : null;
    initialize();
}

// 防止Tomcat.start被调用完且此时spring容器未完成启动就提前接收网络请求,这里先临时移除connectors
private void initialize() throws WebServerException {
    synchronized (this.monitor) {
        try {
            addInstanceIdToEngineName();
            Context context = findContext();
            context.addLifecycleListener((event) -> {
                if (context.equals(event.getSource()) && Lifecycle.START_EVENT.equals(event.getType())) {
                    // Remove service connectors so that protocol binding doesn't happen when the service is started.
                    // 防止Tomcat.start被调用完且此时spring容器未完成启动就提前接收网络请求,这里先临时移除connectors
                    removeServiceConnectors();
                }
            });

            // Start the server to trigger initialization listeners
            this.tomcat.start();

            // We can re-throw failure exception directly in the main thread
            rethrowDeferredStartupExceptions();

            try {
                ContextBindings.bindClassLoader(context, context.getNamingToken(), getClass().getClassLoader());
            } catch (NamingException ex) {
                // Naming is not enabled. Continue
            }

            // Unlike Jetty, all Tomcat threads are daemon threads. We create a blocking non-daemon to stop immediate shutdown
            startDaemonAwaitThread();
        }
        catch (Exception ex) {
            stopSilently();
            destroySilently();
            throw new WebServerException("Unable to start embedded Tomcat", ex);
        }
    }
}

private void removeServiceConnectors() {
    for (Service service : this.tomcat.getServer().findServices()) {
        Connector[] connectors = service.findConnectors().clone();
        // Connector被临时保存起来
        this.serviceConnectors.put(service, connectors);
        for (Connector connector : connectors) {
            service.removeConnector(connector);
        }
    }
}

// WebServer启动时,恢复之前被移除且临时保存起来的Connector,这样就可以接收网络请求了.
public void start() throws WebServerException {
    synchronized (this.monitor) {
        if (this.started) {
            return;
        }
        try {
            // 恢复之前被移除且临时保存起来的Connector
            addPreviouslyRemovedConnectors();
            Connector connector = this.tomcat.getConnector();
            if (connector != null && this.autoStart) {
                performDeferredLoadOnStartup();
            }
            checkThatConnectorsHaveStarted();
            this.started = true;
            logger.info("Tomcat started on port(s): " + getPortsDescription(true) + " with context path '"
                    + getContextPath() + "'");
        } catch (ConnectorStartFailedException ex) {
            stopSilently();
            throw ex;
        } catch (Exception ex) {
            PortInUseException.throwIfPortBindingException(ex, () -> this.tomcat.getConnector().getPort());
            throw new WebServerException("Unable to start embedded Tomcat server", ex);
        } finally {
            Context context = findContext();
            ContextBindings.unbindClassLoader(context, context.getNamingToken(), getClass().getClassLoader());
        }
    }
}

private void addPreviouslyRemovedConnectors() {
    Service[] services = this.tomcat.getServer().findServices();
    for (Service service : services) {
        Connector[] connectors = this.serviceConnectors.get(service);
        if (connectors != null) {
            for (Connector connector : connectors) {
                service.addConnector(connector);
                if (!this.autoStart) {
                    stopProtocolHandler(connector);
                }
            }
            this.serviceConnectors.remove(service);
        }
    }
}
```


