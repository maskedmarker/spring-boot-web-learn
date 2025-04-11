package org.example.learn.spring.boot.web.hello.controller;

import org.example.learn.spring.boot.web.hello.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @GetMapping("/pdf/{id}")
    public HttpEntity<Resource> getPdf(@PathParam("id") String id, @RequestParam(name = "type", defaultValue = "attachment") String type) {
        return downloadService.getPdf(id, type);
    }
}
