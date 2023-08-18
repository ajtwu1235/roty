package com.example.roty.User.controller;


import com.example.roty.User.domain.User;
import com.example.roty.User.service.UserService;
import com.example.roty.security.jwt.AuthService;
import com.example.roty.security.oauth.PrincipalDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final AuthService authService;

    private final UserService userService;

    @GetMapping("/")
    public String in(){

        return "login";
    }
    @GetMapping("/login")
    public String index(){

        return "login";
    }
//
//    @PostMapping("/token")
//    public String getToken(){
//        authService.makeToken()
//    }


    @ResponseBody
    @GetMapping("/test")
    public String test() {



        return "Oauth 로그인 성공";
    }

    @ResponseBody
    @GetMapping("/token")
    public String getToken(Authentication authentication){

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        String token= authService.makeToken(principal.getUser());

        return token;
    }

    @ResponseBody
    @GetMapping("/tokenInfo")
    public  Map<String, Object>  getTokenInfo(@RequestHeader("Authorization") String token){



//        Map<String, Object> claims1 = authService.getClaims("eyJhbGciOiJIUzM4NCJ9.eyJ1c2VySWQiOjEsIm5hbWUiOiLquYDtg5ztmY0iLCJlbWFpbCI6ImFqdHd1MTIzNUBuYXZlci5jb20iLCJleHAiOjE2OTIzNTcwODR9.Ltibeo7xqcY_vXSKGt0eRGT1B-waDZL1IQ7JHQIZAz-lr7yqFWMnuBfDRkWVBEs-");

        return  authService.getClaims(token);
    }
}
