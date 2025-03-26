# spring-boot-web关于tomcat的内部实现

## tomcat启动

ServletWebServerApplicationContext
```text
org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#refresh
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// ...
			try {
				// ...

				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}
			// ...
		}
	}
	
	
	protected void finishRefresh() {
		// Clear context-level resource caches (such as ASM metadata from scanning).
		clearResourceCaches();

		// Initialize lifecycle processor for this context.
		initLifecycleProcessor();

		// Propagate refresh to lifecycle processor first.
		getLifecycleProcessor().onRefresh();

		// Publish the final event.
		publishEvent(new ContextRefreshedEvent(this));

		// Participate in LiveBeansView MBean, if active.
		LiveBeansView.registerApplicationContext(this);
	}	
	
org.springframework.boot.web.servlet.context.WebServerStartStopLifecycle#start	

	public void start() {
		this.webServer.start();
		this.running = true;
		this.applicationContext
				.publishEvent(new ServletWebServerInitializedEvent(this.webServer, this.applicationContext));
	}
```


## data-binding

```text
org.springframework.web.method.annotation.RequestParamMethodArgumentResolver.resolveName(String name, MethodParameter parameter, NativeWebRequest request)

Resolve the given parameter type and value name into an argument value.
Parameters:
name - the name of the value being resolved
parameter - the method parameter to resolve to an argument value (pre-nested in case of a Optional declaration)
request - the current request
Returns:
the resolved argument (may be null)

protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
    HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);

    // 优先解析参数类型为Part/MultipartFile的
    if (servletRequest != null) {
        Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
        if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
            return mpArg;
        }
    }

    // 优先尝试从multipart中获取参数值
    Object arg = null;
    MultipartRequest multipartRequest = request.getNativeRequest(MultipartRequest.class);
    if (multipartRequest != null) {
        List<MultipartFile> files = multipartRequest.getFiles(name);
        if (!files.isEmpty()) {
            arg = (files.size() == 1 ? files.get(0) : files);
        }
    }
    // 次优从
    if (arg == null) {
        String[] paramValues = request.getParameterValues(name);
        if (paramValues != null) {
            arg = (paramValues.length == 1 ? paramValues[0] : paramValues);
        }
    }
    return arg;
}
```