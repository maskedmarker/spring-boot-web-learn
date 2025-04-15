# 



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