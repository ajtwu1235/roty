package com.example.roty.User.service;

import com.example.roty.User.domain.User;
import com.example.roty.User.repository.UserRepository;
import com.example.roty.security.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }


    public Map<String, Object> getTokenToData(String token) {

        return  authService.getClaims(token);
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email);
    }
}
