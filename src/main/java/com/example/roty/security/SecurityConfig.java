package com.example.roty.security;

import com.example.roty.User.repository.UserRepository;
import com.example.roty.User.service.UserService;
//import com.example.roty.security.jwt.JwtAuthenticationFilter;
import com.example.roty.security.jwt.AuthService;
import com.example.roty.security.jwt.JwtAuthenticationFilter;
import com.example.roty.security.jwt.JwtAuthorizationFilter;
import com.example.roty.security.jwt.JwtTokenProvider;

import com.example.roty.security.oauth.OAuth2UserSuccessHandler;
import com.example.roty.security.oauth.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
    private final Oauth2UserService oauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{


        return http.csrf(csrf->csrf.disable())
                //시큐리티 세션 미사용 (인증,인가)
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //폼 로그인 비활성화
                .formLogin(f->f.disable())
                //기존 http방식 비활성화  -> Bearer 토큰
                .httpBasic(h->h.disable())
                .authorizeHttpRequests(r->
                        {
                            r.requestMatchers("/*").permitAll();
                            r.requestMatchers("/user").authenticated();
                        }

                )
                .oauth2Login(oauth->
                        {

                            oauth.loginPage("/login2");
                            oauth.userInfoEndpoint((u)->
                                    u.userService(oauth2UserService));

                            oauth.defaultSuccessUrl("/token");
                        }

                )
                .build();
    }

}
