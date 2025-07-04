package com.scammers.m3bank.services;

import com.scammers.m3bank.models.User;
import com.scammers.m3bank.models.dto.UserCreateRequest;
import com.scammers.m3bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserCreateRequest request) throws RuntimeException {
        validateRegisterUserRequest(request);

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        log.info("User {} registered successfully", user.getEmail());
    }
    private void validateRegisterUserRequest(UserCreateRequest request) throws RuntimeException {
        if (userRepository.findByEmail(request.getEmail()) != null)
            throw new RuntimeException("Пользователь уже существует");
        if (!(request.getPassword().equals(request.getConfirmPassword())))
            throw new RuntimeException("Пароли не совпадают");
    }
}
