package com.example.roty.User.repository;

import com.example.roty.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
