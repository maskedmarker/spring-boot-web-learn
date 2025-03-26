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
		○ Content-Type: multipart/form-data



multipart/form-data中有多个section,每个section可以自定义编码类型

```

## Content-Type

### application/x-www-form-urlencoded
```text
application/x-www-form-urlencoded

application/x-www-form-urlencoded 是一种 MIME 类型，常用于在 HTTP 请求中编码表单数据
表单字段的名称和值会进行 URL 编码（百分比编码），即将非 ASCII 字符和某些特殊字符（如空格、&、=）转换为 % 后跟两个十六进制数的形式。
```

### multipart/form-data

