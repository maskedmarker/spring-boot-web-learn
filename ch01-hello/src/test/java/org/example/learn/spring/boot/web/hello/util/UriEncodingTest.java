package org.example.learn.spring.boot.web.hello.util;

import org.junit.Test;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

/**
 * percent-encoding编码方法适用于将非法字符和保留字符转为合法的字符
 *
 * queryString中的key/value值也采用该转码方式
 * application/x-www-form-urlencoded类型的http请求,http消息体中的key/value值也采用该转码方式
 *
 */
public class UriEncodingTest {

    /**
     * 将字符串用percent-encoding方法转码为为符合URI规范的字符串
     */
    @Test
    public void test0() {
        String queryValue = "中文张三";
        // 将字符串按utf-8的编码方式拆解为一个个code-point的code-unit,将code-unit用percent-encoding方法编码
        String encodedValue = UriUtils.encode(queryValue, StandardCharsets.UTF_8);
        System.out.println("encodedValue = " + encodedValue);
    }

    @Test
    public void test1() {
        String encodedValue = "%E4%B8%AD%E6%96%87%E5%BC%A0%E4%B8%89";
        String queryValue = UriUtils.decode(encodedValue, StandardCharsets.UTF_8);
        System.out.println("queryValue = " + queryValue);
    }

    /**
     * form-data的value值用percent-encoding方法编码,但是不带%前缀
     */
    @Test
    public void test2() {
        String encodedValue = "%E4%B8%AD%E6%96%87%E5%BC%A0%E4%B8%89".replace("%", "");
        String formDataValue = new String(HexUtils.fromHexString(encodedValue), StandardCharsets.UTF_8);
        System.out.println("formDataValue = " + formDataValue);
    }
}
