package org.example.learn.spring.boot.web.hello.controller;

import org.apache.commons.codec.binary.Base64;
import org.example.learn.spring.boot.web.hello.response.MultipartRequestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
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
     * <p>
     * 根据uri的规定,reserved characters和non-ASCII character都要percent-encoded
     */
    @RequestMapping("/queryString")
    @ResponseBody
    public String queryString(HttpServletRequest request) {
        request.getParameterMap().forEach((k, v) -> {
            logger.info("key = {}", k);
            logger.info("value = {}", v);
        });

        return "queryString";
    }

    /**
     * application/x-www-form-urlencoded类型的http请求,将需要传递的参数从url的queryString部分转移到了body中,
     * 但是reserved characters和non-ASCII character都要percent-encoded
     * <p>
     * FormHttpMessageConverter来处理application/x-www-form-urlencoded类型的content-type
     */
    @RequestMapping("/formUrlencoded")
    @ResponseBody
    public String formUrlencoded(HttpServletRequest request) {
        request.getParameterMap().forEach((k, v) -> {
            logger.info("key = {}", k);
            logger.info("value = {}", v);
        });

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

            MediaType mediaType = null;
            if (!StringUtils.isEmpty(contentType)) {
                mediaType = MediaType.parseMediaType(contentType);
            }

            try {
                byte[] bytes = FileCopyUtils.copyToByteArray(part.getInputStream());
                logger.info("value has {} bytes", bytes.length);
                logger.info("value base64 value is {}", Base64.encodeBase64String(bytes));

                // form-data的part没有声明content-type,就使用默认值text/plain
                if (mediaType == null || MediaType.TEXT_PLAIN.isCompatibleWith(mediaType)) {
                    // text/plain 原始二进制数据指的就是字符串的code-unit序列 (这里假定了client使用的是UTF-8)
                    String rawValue = new String(bytes, StandardCharsets.UTF_8);
                    logger.info("rawValue=[{}]", rawValue);
                } else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
                    String rawValue = UriUtils.decode(new String(bytes, StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    logger.info("rawValue=[{}]", rawValue);
                } else if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                    String rawValue = new String(bytes, StandardCharsets.UTF_8);
                    logger.info("rawValue=[{}]", rawValue);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            logger.info("-----------------------");
        });

        MultipartRequestResult requestResult = new MultipartRequestResult();
        requestResult.setData("ok");
        return requestResult;
    }
}
