package org.example.learn.spring.boot.web.proxy.dao;

import org.example.learn.spring.boot.web.proxy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
