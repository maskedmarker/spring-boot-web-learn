
@RequestBody和@ResponseBody注解的预留的aop切入点
RequestBodyAdvice
ResponseBodyAdvice


## ControllerAdvice
```text
-- caller
ExceptionHandlerExceptionResolver
ResponseEntityExceptionHandler

--> callee
org.springframework.web.method.ControllerAdviceBean.findAnnotatedBeans
```