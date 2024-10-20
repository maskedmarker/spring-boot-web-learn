# 关于spring对http的抽象

## http报文

### 普通GET类型的报文
```

```

### 普通POST类型的报文
```

```

### application/x-www-form-urlencoded类型的报文
```
POST /myapp/param/formUrlencoded HTTP/1.1
User-Agent: PostmanRuntime/7.42.0
Accept: */*
Cache-Control: no-cache
Postman-Token: 62643407-0654-4f15-9441-d6838a8b12e5
Host: localhost:9080
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Content-Type: application/x-www-form-urlencoded
Content-Length: 55

key1=zhangSan&key2=%E5%BC%A0%E4%B8%89%E4%B8%AD%E6%96%87
```

### multipart/form-data类型的报文
```
POST /myapp/param/multipart HTTP/1.1
User-Agent: PostmanRuntime/7.42.0
Accept: */*
Cache-Control: no-cache
Postman-Token: e8601cfb-14f2-4aac-8345-230f3338cb62
Host: localhost:9080
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Content-Type: multipart/form-data; boundary=--------------------------741070492237451974675802
Content-Length: 282

----------------------------741070492237451974675802
Content-Disposition: form-data; name="key1"

............(客户端按照utf-8编码方式将英文字符串转换为二进制数据)
----------------------------741070492237451974675802
Content-Disposition: form-data; name="key2"

............(客户端按照utf-8编码方式将中文字符串转换为二进制数据)
----------------------------741070492237451974675802--
Content-Disposition: form-data; name="key3"; filename="................txt"; filename*=UTF-8''%E4%B8%AD%E6%96%87%E6%96%87%E4%BB%B6%E5%90%8D.txt
Content-Type: text/plain

...............(文件的二进制数据)
----------------------------026028807049205168480720--
```

## 核心概念

### spring对http协议的抽象(模型)
#### HttpEntity
Represents an HTTP request or response entity, consisting of headers and body.


#### HttpMessage
Represents the base interface for HTTP request and response messages. Consists of HttpHeaders

##### HttpInputMessage
Represents an HTTP input message, consisting of headers and a readable body.
Typically implemented by an HTTP request handle on the server side, or an HTTP response handle on the client side.

##### HttpOutputMessage
Represents an HTTP output message, consisting of headers and a writable body.
Typically implemented by an HTTP request handle on the client side, or an HTTP response handle on the server side.


HttpMessage与HttpEntity基本上是重叠的概念,为什么会重复定义呢?

### mvc层面

#### HttpMessageConverter
Strategy interface that specifies a converter that can convert from and to HTTP requests and responses.

```
Type parameters: <T> – the converted object type

// Read an object of the given type from the given input message, and returns it.
read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException;

// Write an given object to the given output message.
void write(T t, @Nullable MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException;
```

##### FormHttpMessageConverter
Implementation of HttpMessageConverter to read and write 'normal' HTML forms and also to write (but not read) multipart data (e.g. file uploads).
In other words, this converter can read and write the "application/x-www-form-urlencoded" media type as MultiValueMap<String, String>, 
and it can also write (but not read) the "multipart/form-data" and "multipart/mixed" media types as MultiValueMap<String, Object>.

默认支持如下mimeType:
application/x-www-form-urlencoded
multipart/form-data
multipart/mixed


### request parameters

@RequestParam的javadoc中有如下描述:
In Spring MVC, "request parameters" map to query parameters, form data, and parts in multipart requests.

Supported for annotated handler methods in Spring MVC and Spring WebFlux as follows:
In Spring MVC, "request parameters" map to query parameters, form data, and parts in multipart requests. 
This is because **the Servlet API combines query parameters and form data into a single map called "parameters"**, and that includes automatic parsing of the request body.
In Spring WebFlux, "request parameters" map to query parameters only. 
To work with all 3, query, form data, and multipart data, you can use data binding to a command object annotated with ModelAttribute.


## tomcat解析http报文请求参数
以tomcat代码举例,
如果content-type为x-www-form-urlencoded时,当调用ServletRequest.getParameter之类的方法时,
    会触发tomcat读取整个http的queryString二进制数据,然后以默认(iso8859-1)或用户指定(通过ServletRequest.setCharacterEncoding设置)的编码方式按percent-encoded解码.
    会触发tomcat读取整个http的body的二进制数据,然后以默认(iso8859-1)或用户指定(通过ServletRequest.setCharacterEncoding设置)的编码方式按percent-encoded解码.
如果content-type为multipart/form-data时,当调用HttpServletRequest.getParts的方法时,
    会触发tomcat读取整个http的body二进制数据,并按照boundary将body切分为多个DiskFileItem(此时还是以二进制对待),
    然后读取DiskFileItem的二进制数据,
        如果DiskFileItem没有filename,就认为是普通的表单字符串数据,按照request的编码(通过ServletRequest.setCharacterEncoding设置)转换为字符串.
        如果DiskFileItem有filename,就认为是普通的表单文件数据,按照request的编码转换为字符串.

multipart/form-data或x-www-form-urlencoded的数据解析后都会被放到Params中,算作"request parameters".


## spring-mvc解析http报文
spring-mvc解析http报文是基于tomcat(准确讲servlet)的解析基础.但是spring-mvc又做了更加实用的封装.
对于multipart类型的报文,spring-mvc将文本类型的request parameter和文件类型的request parameter做了区分.
具体实现是StandardServletMultipartResolver用StandardMultipartHttpServletRequest封装原来的HttpServletRequest.




RequestParamMethodArgumentResolver