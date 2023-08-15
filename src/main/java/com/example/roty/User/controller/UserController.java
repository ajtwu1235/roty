package com.example.roty.User.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping
public class UserController {
    @GetMapping
    public String index(){
        return "login";
    }
}
