package org.example.learn.spring.boot.web.hello.controller;

import org.example.learn.spring.boot.web.hello.request.MultipartRequestParam;
import org.example.learn.spring.boot.web.hello.response.MultipartRequestResult;
import org.example.learn.spring.boot.web.hello.util.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/param")
public class RequestParamController {

    private static final Logger logger = LoggerFactory.getLogger(RequestParamController.class);

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
        logger.info("key1 = {}", key1);
        logger.info("key2 = {}", key2);

        String value2 = request.getParameter("key2");
        logger.info("value2:{}", value2);

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
        logger.info("key1 = {}", key1);
        logger.info("key2 = {}", key2);

        String value2 = request.getParameter("key2");
        logger.info("value2:{}", value2);

        return "formUrlencoded";
    }

    /**
     * 也可以通过HttpServletRequest来接收客户端发来的数据
     */
    @RequestMapping("/multipart")
    @ResponseBody
    public MultipartRequestResult multipart(HttpServletRequest request) throws ServletException, IOException {
        logger.info("request:{}", request);
        Collection<Part> parts = request.getParts();
        parts.forEach(part -> {
            String name = part.getName();
            Collection<String> headerNames = part.getHeaderNames();
            List<Collection<String>> headerNameValues = headerNames.stream().map(part::getHeaders).collect(Collectors.toList());
            logger.info("---------part name:{}--------------", name);
            logger.info("headerNames={} headerNameValues={}", headerNames, headerNameValues);
            String contentType = part.getContentType();
            if (StringUtils.isEmpty(contentType)) {
                try {
                    // form-data不用percent-encoding,无论是字符串还是文件,传输的都是原始二进制.
                    // 对于字符串,原始二进制指的就是code-unit序列了
                    byte[] bytes = FileCopyUtils.copyToByteArray(part.getInputStream());
                    String rawValue = new String(bytes, StandardCharsets.UTF_8);
                    logger.info("rawValue={}", rawValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            logger.info("-----------------------");
        });

        MultipartRequestResult requestResult = new MultipartRequestResult();
        requestResult.setData("ok");
        return requestResult;
    }

    /**
     * 也可以通过MultipartHttpServletRequest来接收客户端发来的数据
     */
    @RequestMapping("/multipart2")
    @ResponseBody
    public MultipartRequestResult multipart(MultipartHttpServletRequest request) {
        logger.info("request:{}", request);

        MultipartRequestResult requestResult = new MultipartRequestResult();
        requestResult.setData("ok");
        return requestResult;
    }

    /**
     * 通过@ModelAttribute注解来进行数据绑定
     */
    @RequestMapping("/multipart3")
    @ResponseBody
    public MultipartRequestResult multipart(HttpServletRequest request, @ModelAttribute MultipartRequestParam multipartRequestParam) {
        logger.info("request:{}", request);

        MultipartRequestResult requestResult = new MultipartRequestResult();
        requestResult.setData("ok");
        return requestResult;
    }
}
