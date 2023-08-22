package com.example.roty.User.controller;


import com.example.roty.User.domain.User;
import com.example.roty.User.service.UserService;
import com.example.roty.security.oauth.AuthService;
import com.example.roty.security.oauth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    @GetMapping()
    public String index(){
        return "login";
    }

    @ResponseBody
    @GetMapping("/test")
    public String test(){ return "test";}

    @ResponseBody
    @GetMapping("/test2")
    public User test2(){ return User.builder().userId(1L)
            .email("dafdfa@naver.com")
            .role("MEMBER").build();}

    //토큰발급 경로
    @GetMapping("/token")
    public String getToken(Authentication authentication){

//        if(authentication==null){
//            return "redirect:/oauth2/authorization/kakao";
//        }

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        String token= authService.makeToken(principal.getUser());

        return "redirect:http://localhost:3000/redirect?token="+token;
    }



    // 시큐리티에서 꺼낸걸로 바로 받아보기
    @ResponseBody
    @GetMapping("/user")
    public User getUser(@AuthenticationPrincipal PrincipalDetails principalDetails){

        if(principalDetails==null){
            return null;
        }


        return principalDetails.getUser();
    }

    //토큰으로 꺼내서 받아보기
    @ResponseBody
    @GetMapping("/tokenInfo")
    public Map<String,Object> getToken(@RequestHeader("Authorization") String token){


            return authService.getClaims(token.replace("Bearer ",""));
    }


}
