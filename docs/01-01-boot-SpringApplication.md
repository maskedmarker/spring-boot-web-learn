# spring-boot启动

spring-boot的启动流程在SpringApplication类中.

注意区分如下几个概念
```text
BeanFactory
    功能单一,主要用来管理bean的.
    通常需要动态管理bean的,常使用子类ConfigurableListableBeanFactory

ApplicationContext
    功能职责比较多.不仅要管理要维护bean,还能发布消息等,即通常将的spring容器
    通常需要动态管理bean的,常使用子类ConfigurableApplicationContext

SpringApplication
    spring-boot的启动类,将复杂的spring容器管理/自动加载等封装到内部
```


## spring-boot启动过程


### 判断哪种运行环境

spring-boot可以在3中环境中运行,即无web环境/servlet-web环境/reactive-web环境.
当spring-boot确定了运行环境后,spring-boot的启动类SpringApplication的主要属性也会做响应的调整.

SpringApplication.run启动初始阶段时,会基于classpath的信息来确定使用哪种运行环境(即WebApplicationType)
```text
org.springframework.boot.SpringApplication.SpringApplication(org.springframework.core.io.ResourceLoader, java.lang.Class<?>...)
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
    // ...
    this.webApplicationType = WebApplicationType.deduceFromClasspath();
    // ...
}

org.springframework.boot.WebApplicationType.deduceFromClasspath
static WebApplicationType deduceFromClasspath() {
    if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
            && !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
        return WebApplicationType.REACTIVE;
    }
    for (String className : SERVLET_INDICATOR_CLASSES) {
        if (!ClassUtils.isPresent(className, null)) {
            return WebApplicationType.NONE;
        }
    }
    return WebApplicationType.SERVLET;
}
```