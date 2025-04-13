package org.example.learn.spring.boot.web.hello.controller;

import org.example.learn.spring.boot.web.hello.request.MultipartReq;
import org.example.learn.spring.boot.web.hello.request.MultipartRequestParam;
import org.example.learn.spring.boot.web.hello.response.MultipartRequestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/binding")
public class MvcBindingController {

    private static final Logger logger = LoggerFactory.getLogger(MvcBindingController.class);

    /**
     * url的queryString包含需要传递的参数
     *
     * 根据uri的规定,reserved characters和non-ASCII character都要percent-encoded
     *
     *
     */
    @RequestMapping("/queryString")
    @ResponseBody
    public String queryString(@RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        logger.info("key1 = {}", key1);
        logger.info("key2 = {}", key2);

        return "queryString";
    }

    /**
     * application/x-www-form-urlencoded类型的http请求,将需要传递的参数从url的queryString部分转移到了body中,
     * 但是reserved characters和non-ASCII character都要percent-encoded
     *
     * FormHttpMessageConverter来处理application/x-www-form-urlencoded类型的content-type
     */
    @RequestMapping(path = "/formUrlencoded", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public String formUrlencoded(@RequestParam("key1") String key1, @RequestParam("key2") String key2) {
        logger.info("key1 = {}", key1);
        logger.info("key2 = {}", key2);

        return "formUrlencoded";
    }

    /**
     * 也可以通过MultipartHttpServletRequest来接收客户端发来的数据
     *
     * 注意:
     * 当使用multipart/form-data时,不能使用@RequestBody,因为@RequestBody的语义是将body当作一个整体,所以会发现没有合适的HttpMessageConverter
     */
    @RequestMapping(path = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public MultipartRequestResult multipart(@ModelAttribute MultipartReq multipartReq) {
        logger.info("multipartReq:{}", multipartReq);

        MultipartRequestResult requestResult = new MultipartRequestResult();
        requestResult.setData("ok");
        return requestResult;
    }

    /**
     * 通过@ModelAttribute注解来进行数据绑定
     */
    @RequestMapping(path = "/multipart2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public MultipartRequestResult multipart(@ModelAttribute MultipartRequestParam multipartRequestParam) {
        MultipartRequestResult requestResult = new MultipartRequestResult();
        requestResult.setData("ok");
        return requestResult;
    }
}
