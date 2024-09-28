# 关于spring-boot-web的学习

spring-boot的autoconfigure对web配置与spring-web有差异.





#### DispatcherServlet初始化
DispatcherServlet的初始化逻辑是放在了其所管理的spring容器首次初始化完成时发生的,即重写了FrameworkServlet.onRefresh方法.
DispatcherServlet主要的初始化逻辑在方法initStrategies中.
initStrategies都做了哪些工作:
1. 从spring容器中获取MultipartResolver对象,并注入到DispatcherServlet属性中.
2. 从spring容器中获取LocaleResolver对象,并注入到DispatcherServlet属性中.
3. 从spring容器中获取ThemeResolver对象,并注入到DispatcherServlet属性中.
4. 从spring容器中获取HandlerMapping对象,并注入到DispatcherServlet属性中.
5. 从spring容器中获取HandlerAdapter对象,并注入到DispatcherServlet属性中.
6. 从spring容器中获取HandlerExceptionResolver对象,并注入到DispatcherServlet属性中.
7. 从spring容器中获取RequestToViewNameTranslator对象,并注入到DispatcherServlet属性中.
8. 从spring容器中获取ViewResolver对象,并注入到DispatcherServlet属性中.
9. 从spring容器中获取FlashMapManager对象,并注入到DispatcherServlet属性中.

上面的这些被依赖的对象基本都是由WebMvcAutoConfiguration.EnableWebMvcConfiguration向spring容器中添加的.



注意:
1. 如果controller被同时添加@Controller和@Async时.
    - RequestMappingHandlerMapping正常收集metadata时,HandlerMethod.beanType是proxy类
    - RequestMappingHandlerMapping在处理请求时,HandlerMethod.bean是proxy实例
    - aop作用发生在spring容器生成bean时,就已经是proxy类实例了.