package org.example.learn.spring.boot.web.proxy.facade;


import org.example.learn.spring.boot.web.proxy.autoconfigure.ExportService;
import org.example.learn.spring.boot.web.proxy.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ExportService
public interface IHelloService {

    @PostMapping("/findAllUsers")
    public List<User> findAllUsers();

}