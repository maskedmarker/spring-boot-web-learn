package org.example.learn.spring.boot.web.proxy.controller;

import org.example.learn.spring.boot.web.proxy.model.User;
import org.example.learn.spring.boot.web.proxy.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return helloService.findAllUsers();
    }
}
