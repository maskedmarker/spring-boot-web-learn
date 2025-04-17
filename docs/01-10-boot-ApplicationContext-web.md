# spring-boot-web-servlet

spring-boot当判定运行环境为servlet-web环境时(即WebApplicationType.SERVLET),spring-boot的主类SpringApplication的主要属性也会做响应的调整.
SpringApplication将AnnotationConfigServletWebServerApplicationContext作为spring容器,用来容纳所需的bean.
```text
ConfigurableEnvironment --> StandardServletEnvironment
ConfigurableApplicationContext --> AnnotationConfigServletWebServerApplicationContext
```

## AnnotationConfigServletWebServerApplicationContext

AnnotationConfigServletWebServerApplicationContext是AbstractApplicationContext的子类,
ConfigurableApplicationContext.refresh的主要逻辑流程在AbstractApplicationContext.refresh方法中.

### AbstractApplicationContext.refresh

为了方便记忆, 简化AbstractApplicationContext.refresh的执行逻辑

```text
org.springframework.context.support.AbstractApplicationContext.refresh

	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {

			// Prepare this context for refreshing.
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			// ApplicationContext是个多功能的复合体,其内部会有一个功能纯粹的ConfigurableListableBeanFactory,专门用来管理bean的.
			// 方法名比较匹配, 如果有就做好初始化,如果没有就创建,总之是一个新鲜的BeanFactory
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			// ApplicationContext中对ConfigurableListableBeanFactory处理的公共逻辑
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
				// spring框架预留给自己的对ConfigurableListableBeanFactory操作的扩展接口
				// ApplicationContext子类对ConfigurableListableBeanFactory处理的的个性化逻辑
				postProcessBeanFactory(beanFactory);

				// Invoke factory processors registered as beans in the context.
				// BeanFactoryPostProcessor作为框架预留给用户对ConfigurableListableBeanFactory操作的扩展接口(这里也是各种starter的扩展点)
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				// 在普通bean之前,提前实例化BeanPostProcessor,这样才能发挥BeanPostProcessor的功能
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.(不太重要,主要是i18n相关的)
				initMessageSource();

				// Initialize event multicaster for this context.
				// 向ConfigurableListableBeanFactory中提前实例化ApplicationEventMulticaster,
				// 发布事件时,ApplicationContext只对接ApplicationEventMulticaster,ApplicationEventMulticaster负责将事件广播给各个listener(即由其分别调用各个listener的方法)
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
				onRefresh();

				// Check for listener beans and register them.
				// 在普通bean之前,又一次提前实例化ApplicationListener
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons. 
				// 这里会将容器中的普通bean实例化并初始化
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				// 这里会发布ContextRefreshedEvent事件,还会通知调用LifecycleProcessor.onRefresh方法(基于方法名来通知事件),LifecycleProcessor将会调用所有Lifecycle对象的start方法	
				finishRefresh();
			} catch (BeansException ex) {
				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			} finally {
				// Reset common introspection caches in Spring's core, since we might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}
```


### AnnotationConfigServletWebServerApplicationContext

AnnotationConfigServletWebServerApplicationContext是ServletWebServerApplicationContext的子类.
ServletWebServerApplicationContext扩展了普通的容器接口ConfigurableApplicationContext
```text
// refresh执行中创建WebServer
protected void onRefresh() {
    super.onRefresh();
    try {
        createWebServer();
    }
    catch (Throwable ex) {
        throw new ApplicationContextException("Unable to start web server", ex);
    }
}

private void createWebServer() {
    WebServer webServer = this.webServer;
    ServletContext servletContext = getServletContext();
    // 如果没有指定webServer
    if (webServer == null && servletContext == null) {
        StartupStep createWebServer = this.getApplicationStartup().start("spring.boot.webserver.create");
        ServletWebServerFactory factory = getWebServerFactory();
        createWebServer.tag("factory", factory.getClass().toString());
        // 创建webServer时,预留了initializer类做初始化定制
        this.webServer = factory.getWebServer(getSelfInitializer());
        createWebServer.end();
        // 向spring容器中注册WebServerGracefulShutdownLifecycle,这样在spring容器关闭时回调SmartLifecycle.stop()方法进而调用webServer.stop()
        getBeanFactory().registerSingleton("webServerGracefulShutdown", new WebServerGracefulShutdownLifecycle(this.webServer));
        // 向spring容器中注册WebServerStartStopLifecycle,这样容器启动后自动调用回调SmartLifecycle.start()方法进而调用webServer.start()(对于tomcat,从而触发tomcat的启动)
        getBeanFactory().registerSingleton("webServerStartStop", new WebServerStartStopLifecycle(this, this.webServer));
    } else if (servletContext != null) { //如果指定了webServer且假定已经做好了webServer的初始化工作
        try {
            // 将spring容器中与servlet容器相关的servlets, filters, listeners context-params and attributes necessary for initialization.
            // 具体实现类有RegistrationBean/SessionConfiguringInitializer
            getSelfInitializer().onStartup(servletContext);
        } catch (ServletException ex) {
            throw new ApplicationContextException("Cannot initialize servlet context", ex);
        }
    }
    // 将容器中的servlet相关的占位符替换为真实的servlet值
    initPropertySources();
}

public final void refresh() throws BeansException, IllegalStateException {
    try {
        // 正常的refresh执行流程
        super.refresh();
    }
    catch (RuntimeException ex) {
        // refresh执行异常,要将webServer停掉
        WebServer webServer = this.webServer;
        if (webServer != null) {
            webServer.stop();
        }
        throw ex;
    }
}
```



