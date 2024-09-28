package org.example.learn.spring.boot.web.proxy.service;


import org.example.learn.spring.boot.web.proxy.facade.IHelloService;
import org.example.learn.spring.boot.web.proxy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelloService implements IHelloService {

    @Autowired
    UserService userService;

    @Override
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

}