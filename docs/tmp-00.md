# http 基础知识


## 编码
```text
消息体（Body）
HTTP请求的消息体的编码方式取决于内容类型（Content-Type）头部字段所指定的MIME类型。常见的编码方式包括：
• 文本内容:
    ○ Content-Type: text/plain; charset=utf-8
    ○ Content-Type: application/json; charset=utf-8
在这种情况下，消息体的字符编码由charset参数指定，通常是UTF-8。
• 表单数据:
    ○ Content-Type: application/x-www-form-urlencoded
    ○ Content-Type: multipart/form-data; boundary=----WebKitFormBoundary0OAtEIyALH5mA4tG


multipart/form-data中有多个section,每个section可以自定义编码类型

```

## Content-Type

### application/x-www-form-urlencoded
```text
application/x-www-form-urlencoded

application/x-www-form-urlencoded 是一种 MIME 类型,当表单元素不包含文件时,采用的编码方式
表单字段的名称和值会进行URL编码（百分比编码），即将非 ASCII 字符和某些特殊字符（如空格、&、=）的code-point的每个code-unit转换为两个hex字符(并在这两个字符前带上%前缀)。
```

### multipart/form-data
```text
multipart/form-data

multipart/form-data是一种 MIME 类型,当表单元素包含文件时,采用的编码方式
允许各个body part再次声明各自的header

multipart/form-data不使用百分比编码编码.
```

#### Why Percent-Encoding is NOT Needed in multipart/form-data
```text
percent-encoding (URL encoding) is not required for non-ASCII characters in a multipart/form-data HTTP request. 
Unlike application/x-www-form-urlencoded, which requires percent-encoding, multipart/form-data allows raw binary data, including UTF-8 encoded non-ASCII characters, to be sent directly in the request body.

Why Percent-Encoding is NOT Needed in multipart/form-data
Text fields in multipart/form-data are not part of the URL
    application/x-www-form-urlencoded requires percent-encoding because the data is sent in the URL-encoded format (e.g., name=%E5%BC%A0%E4%B8%89).
    multipart/form-data sends data as raw content in the request body, where non-ASCII characters are allowed without encoding.

Multipart boundaries separate fields, avoiding the need for encoding
    Each field in multipart/form-data is enclosed within a boundary, meaning special characters (including spaces and Unicode text) can be safely transmitted.
```









### Content-Disposition

Content-Disposition在请求和响应中表达的含义是不同的.

When uploading files via an HTML form with multipart/form-data, Content-Disposition is used to define each part of the request.
Content-Disposition header in the response is used to specify how content should be presented to the user.

请求中:
```text
Content-Disposition: form-data; name="file"; filename="upload.jpg"
Content-Type: image/jpeg
```
form-data: Indicates that this is form data.
name="file": The name of the form field.
filename="upload.jpg": The original filename of the uploaded file.
Content-Type: image/jpeg: Specifies the file's MIME type.


响应中:
```text
HTTP/1.1 200 OK
Content-Type: application/octet-stream
Content-Disposition: attachment; filename="report.pdf"
```
attachment: Instructs the browser to download the file rather than display it inline.
filename="example.txt": Suggests a default filename for the downloaded file.


## html知识点

form标签的属性
```text
The following attributes control behavior during form submission.

action
The URL that processes the form submission. 
This attribute is ignored when method="dialog" is set.

enctype
If the value of the method attribute is post, enctype is the MIME type of the form submission. 
Possible values:
application/x-www-form-urlencoded: The default value.
multipart/form-data: Use this if the form contains <input> elements with type=file.
text/plain: Useful for debugging purposes.


method
The HTTP method to submit the form with. The only allowed methods/values are (case insensitive):
post: The POST method; form data sent as the request body.
get (default): The GET; form data appended to the action URL with a ? separator. Use this method when the form has no side effects.
dialog: When the form is inside a <dialog>, closes the dialog and causes a submit event to be fired on submission, without submitting data or clearing the form.
```