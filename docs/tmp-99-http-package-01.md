# http报文

oneNote上记录了类型繁多且清晰的http报文,并配有详尽的解释!!!!



## multipart/form-data

请求
```text
POST /myapp/param/multipart HTTP/1.1
Host: localhost:9080
Connection: keep-alive
Content-Length: 2311
Cache-Control: max-age=0
sec-ch-ua: "Google Chrome";v="131", "Chromium";v="131", "Not_A Brand";v="24"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Origin: http://localhost:9080
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary0OAtEIyALH5mA4tG
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Referer: http://localhost:9080/myapp/static/web/http/multipart.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
Cookie: be5713b7=2db90bb6-92cb-4b37-a68d-4eda8bb1b8e9

------WebKitFormBoundary0OAtEIyALH5mA4tG
Content-Disposition: form-data; name="username"

1029224204
------WebKitFormBoundary0OAtEIyALH5mA4tG
Content-Disposition: form-data; name="email"

1029224204@qq.com
------WebKitFormBoundary0OAtEIyALH5mA4tG
Content-Disposition: form-data; name="password"

123
------WebKitFormBoundary0OAtEIyALH5mA4tG
Content-Disposition: form-data; name="confirmPassword"

123
------WebKitFormBoundary0OAtEIyALH5mA4tG
Content-Disposition: form-data; name="gender"

male
------WebKitFormBoundary0OAtEIyALH5mA4tG
Content-Disposition: form-data; name="bio"


------WebKitFormBoundary0OAtEIyALH5mA4tG
Content-Disposition: form-data; name="headPhoto"; filename="test.jpg"
Content-Type: image/jpeg

......(不可见二进制数据)
------WebKitFormBoundary0OAtEIyALH5mA4tG--
```

响应
```text
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 29 Mar 2025 11:44:07 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{"data":"ok"}
```