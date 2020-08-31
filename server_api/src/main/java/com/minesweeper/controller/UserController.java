package com.minesweeper.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minesweeper.security.LoginInfo;
import com.minesweeper.service.UserService;

@RestController
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(value = "/minesweeper/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void register(String username, String password) {
        service.register(username, password);
    }

    @PostMapping(value = "/minesweeper/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public LoginInfo login(String username, String password) {
        return service.login(username, password);
    }
}
