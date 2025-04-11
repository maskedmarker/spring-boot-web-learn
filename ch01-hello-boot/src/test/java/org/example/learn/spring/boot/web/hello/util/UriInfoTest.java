package org.example.learn.spring.boot.web.hello.util;

import org.junit.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


/**
 *
 */
public class UriInfoTest {

    @Test
    public void test0() {
        String url = "https://www.baidu.com/my?id=zhangsan";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        UriComponents uriComponents = uriComponentsBuilder.build();
        System.out.println("uriComponents.getScheme() = " + uriComponents.getScheme());
        System.out.println("uriComponents.getHost() = " + uriComponents.getHost());
        System.out.println("uriComponents.getPort() = " + uriComponents.getPort());
        System.out.println("uriComponents.getPath() = " + uriComponents.getPath());
        System.out.println("uriComponents.getQuery() = " + uriComponents.getQuery());

        uriComponents.getQueryParams().forEach((k, v) -> {
            System.out.println(String.format("key=%s,value=%s", k, v));
        });
    }
}
