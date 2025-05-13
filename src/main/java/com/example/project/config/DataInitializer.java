package com.example.project.config;

import com.example.project.user.entity.User;
import com.example.project.user.enums.Role;
import com.example.project.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User user = new User("admin", passwordEncoder.encode("admin"), "admin");
        user.updateRoles(Role.ADMIN);
        userRepository.save(user);
    }
}
