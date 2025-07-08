package com.scammers.m3bank.components;

import com.scammers.m3bank.enums.Role;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DevUserInitializer {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @PostConstruct
    public void createAdmin() {
        if (repo.findByEmail("admin@example.com") == null) {
            User admin = new User();
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRole(Role.ROLE_ADMIN);
            repo.save(admin);
        }
    }
}

