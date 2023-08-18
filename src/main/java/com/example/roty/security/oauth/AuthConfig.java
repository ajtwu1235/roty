package com.example.roty.security.oauth;

import com.example.roty.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final UserService userService;
    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);

//        provider.setUserDetailsService(oauth2UserService);
//        provider.setO
//        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
