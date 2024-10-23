package org.example.learn.spring.boot.web.hello.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;

public class PathTest {

    @Test
    public void test01() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String pattern = "users/**";
        String uri = "users/getAllUser.json";
        boolean match = pathMatcher.match(pattern, uri);
        System.out.println("match = " + match);
        Assert.assertTrue(match);
    }

    @Test
    public void test02() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String pattern = "users/**";
        String uri = "users/xxx/getAllUser.json";
        boolean match = pathMatcher.match(pattern, uri);
        System.out.println("match = " + match);
        Assert.assertTrue(match);
    }

    @Test
    public void test11() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String pattern = "users/*";
        String uri = "users/getAllUser.json";
        boolean match = pathMatcher.match(pattern, uri);
        System.out.println("match = " + match);
        Assert.assertTrue(match);
    }

    @Test
    public void test12() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String pattern = "users/*";
        String uri = "users/xxx/getAllUser.json";
        boolean match = pathMatcher.match(pattern, uri);
        System.out.println("match = " + match);
        Assert.assertFalse(match);
    }
}
