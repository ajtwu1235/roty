package com.example.roty.security;

import com.example.roty.User.repository.UserRepository;
import com.example.roty.User.service.UserService;
//import com.example.roty.security.jwt.JwtAuthenticationFilter;
import com.example.roty.security.jwt.JwtAuthorizationFilter;
import com.example.roty.security.oauth.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

//    private final UserService userService;

    private final Oauth2UserService oauth2UserService;
    private final UserService userService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{


        return http.csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                //시큐리티 세션 일부사용 (인증,인가)
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                //폼 로그인 비활성화
                .formLogin(f->f.disable())
                //기존 http방식 비활성화  -> Bearer 토큰
                .httpBasic(h->h.disable())
                .authorizeHttpRequests(r->{
                    r.anyRequest().permitAll();
//                    r.requestMatchers("/token").permitAll();
//                    r.anyRequest().permitAll();
                })

                //로그인 성공하면 토큰 받기
                .oauth2Login(oauth->
                oauth.userInfoEndpoint((u)->
                        u.userService(new Oauth2UserService(userRepository,authenticationManager)))
                        .loginPage("http://localhost:8080/oauth2/authorization/kakao")
                        .defaultSuccessUrl("/token")
                )
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))


//                .addFilter(new JwtAuthenticationFilter(authenticationManager))
//                .addFilter(new JwtAuthorizationFilter(authenticationManager))


//                .authorizeHttpRequests(r ->
//                        r.requestMatchers("/").hasRole("ROLE_USER")
//
//                )


                .build();
    }

}
