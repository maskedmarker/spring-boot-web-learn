package org.example.learn.spring.boot.web.hello.controller;

import org.example.learn.spring.boot.web.hello.model.RequestData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/param")
public class RequestParamController {

    /**
     * url的queryString包含需要传递的参数
     *
     * 根据uri的规定,reserved characters和non-ASCII character都要percent-encoded
     *
     *
     */
    @RequestMapping("/queryString")
    @ResponseBody
    public String queryString(HttpServletRequest request, @RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        System.out.println("key1 = " + key1);
        System.out.println("key2 = " + key2);

        String value2 = request.getParameter("key2");
        System.out.println("value2 = " + value2);

        return "queryString";
    }

    /**
     * application/x-www-form-urlencoded类型的http请求,将需要传递的参数从url的queryString部分转移到了body中,
     * 但是reserved characters和non-ASCII character都要percent-encoded
     *
     * FormHttpMessageConverter来处理application/x-www-form-urlencoded类型的content-type
     */
    @RequestMapping("/formUrlencoded")
    @ResponseBody
    public String formUrlencoded(HttpServletRequest request, @RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        System.out.println("key1 = " + key1);
        System.out.println("key2 = " + key2);

        String value2 = request.getParameter("key2");
        System.out.println("value2 = " + value2);

        return "formUrlencoded";
    }

    /**
     * 通过@ModelAttribute注解来进行数据绑定
     */
    @RequestMapping("/multipart")
    @ResponseBody
    public String multipart(HttpServletRequest request, @ModelAttribute RequestData requestData) {
        System.out.println("request = " + request);
        return "hello";
    }

    /**
     * 也可以通过MultipartHttpServletRequest来接收客户端发来的数据
     */
    @RequestMapping("/multipart2")
    @ResponseBody
    public String multipart2(MultipartHttpServletRequest request) {
        System.out.println("request = " + request);
        return "hello";
    }
}
