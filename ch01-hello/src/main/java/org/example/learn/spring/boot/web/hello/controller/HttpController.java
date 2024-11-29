package org.example.learn.spring.boot.web.hello.controller;

import org.example.learn.spring.boot.web.hello.util.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/http")
public class HttpController {

    @GetMapping("/echo")
    public HttpEntity<String> echo(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        Map<String, String[]> parameterMap = request.getParameterMap();

        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("requestURL", requestURL);
        bodyData.put("queryString", queryString);
        bodyData.put("parameterMap", parameterMap);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = JsonUtils.toJsonString(bodyData);

        return new HttpEntity<>(body, httpHeaders);
    }

}
