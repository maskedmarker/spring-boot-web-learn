package org.example.learn.spring.boot.web.hello.req;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送不同类型的content-type
 */
public class Ch02MvcBindingTest {

    /**
     * httpclient的日志初始化可以通过jvm启动时添加系统参数,也可以启动后设置
     * 打印日志的系统参数
     * -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog
     * -Dorg.apache.commons.logging.simplelog.showdatetime=true
     * -Dorg.apache.commons.logging.simplelog.log.org.apache.http=DEBUG
     * -Dorg.apache.commons.logging.simplelog.log.org.apache.http.wire=ERROR
     */
    @Before
    public void setup() {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "INFO");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
    }

    // application/x-www-form-urlencoded类型的post请求
    @Test
    public void test01() throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String url = "http://localhost:9080/myapp/binding/formUrlencoded";
            // 创建HttpPost实例，设置目标URL
            HttpPost httpPost = new HttpPost(url);

            // 创建参数列表
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("key1", "value1"));
            params.add(new BasicNameValuePair("key2", "中文"));

            // 设置请求参数为application/x-www-form-urlencoded格式
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                System.out.println(response.getStatusLine());
                HttpEntity responseEntity = response.getEntity();
                // 不设置编码,就依据HttpEntity的ContentType的mimeType来决定,
                // 如果还不确定,就用http默认编码ISO_8859_1
                // application/json类型mimeType的编码是UTF-8
                String responseBody = EntityUtils.toString(responseEntity);

                // 注意:返回的json数据中,中文用的是\u4e2d\u6587
                System.out.println(responseBody);
            }
        }
    }

    // multipart/form-data类型的post请求
    @Test
    public void test11() throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // 创建HttpPost实例，设置目标URL
            HttpPost httpPost = new HttpPost("http://httpbin.org/anything");

            // 通过MultipartEntityBuilder来创建MultipartFormEntity
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            // 添加普通字段
            builder.addTextBody("field1", "value1", ContentType.TEXT_PLAIN); // 不设置编码就用text/plain的编码ISO_8859_1
            builder.addTextBody("field2", URLEncoder.encode("中文", StandardCharsets.UTF_8.name()), ContentType.APPLICATION_FORM_URLENCODED);
            builder.addTextBody("field3", "{\"name\": \"zhangsan\"}", ContentType.APPLICATION_JSON);

            // 添加文件字段
            File file = new File("src/test/resources/文件名.txt");
            builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());

            // 构建HttpEntity
            HttpEntity multipart = builder.build();
            // 将HttpEntity设置到HttpPost实例中
            httpPost.setEntity(multipart);
            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                System.out.println(response.getStatusLine());
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println(responseBody);
            }
        }
    }

    @Test
    public void test21() throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String url = "http://localhost:9080/myapp/binding/multipart";
            // 创建HttpPost实例，设置目标URL
            HttpPost httpPost = new HttpPost(url);

            // 通过MultipartEntityBuilder来创建MultipartFormEntity
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.RFC6532);
            builder.setCharset(StandardCharsets.UTF_8);
            // 添加普通字段
            builder.addTextBody("username", "zhangsan", ContentType.TEXT_PLAIN); // 不设置编码就用text/plain的编码ISO_8859_1
            builder.addTextBody("password", URLEncoder.encode("中文", StandardCharsets.UTF_8.name()), ContentType.APPLICATION_FORM_URLENCODED);
            builder.addTextBody("bio", "{\"name\": \"张三\"}", ContentType.APPLICATION_JSON);

            // 添加文件字段
            File file = new File("src/test/resources/文件名.txt");
            builder.addBinaryBody("headPhoto", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());

            // 构建HttpEntity
            HttpEntity multipart = builder.build();
            // 将HttpEntity设置到HttpPost实例中
            httpPost.setEntity(multipart);
            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                System.out.println(response.getStatusLine());
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println(responseBody);
            }
        }
    }

    @Test
    public void test22() {
        // Content-Disposition: form-data; name="headPhoto"; filename="初等函数微积分.png"\r\n
        String base64Str = "Q29udGVudC1EaXNwb3NpdGlvbjogZm9ybS1kYXRhOyBuYW1lPSJoZWFkUGhvdG8iOyBmaWxlbmFtZT0i5Yid562J5Ye95pWw5b6u56ev5YiGLnBuZyINCg==";
        byte[] bytes = Base64.decodeBase64(base64Str);
        String string = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("string = [" + string + "]");
    }

    @Test
    public void test23() {
        // 中文张三
        String base64Str = "5Lit5paH5byg5LiJ";
        byte[] bytes = Base64.decodeBase64(base64Str);
        String string = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("string = [" + string + "]");
    }

    @Test
    public void test24() {
        String CRFL = "\r\n";
        byte[] bytes = CRFL.getBytes(StandardCharsets.UTF_8);
        System.out.println("Hex.encodeHexString(bytes) = " + Hex.encodeHexString(bytes)); // 0d0a

        String str = "Content-Disposition: form-data; name=\"headPhoto\"; filename=\"初等函数微积分.png\"\r\n";
        byte[] bytes2 = str.getBytes(StandardCharsets.UTF_8);
        System.out.println("Base64.encodeBase64(bytes2) = " + Base64.encodeBase64String(bytes2));
    }
}
