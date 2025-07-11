package com.scammers.m3bank.services;

import com.scammers.m3bank.annotations.Auditable;
import com.scammers.m3bank.enums.Role;
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

    @Auditable(action = "Регистрация пользователя")
    public void registerUser(UserCreateRequest request) throws RuntimeException {
        validateRegisterUserRequest(request);

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
        log.info("User {} registered successfully", user.getEmail());
    }
    @Auditable(action = "Проверка отравленных данных")
    private void validateRegisterUserRequest(UserCreateRequest request) throws RuntimeException {
        if (userRepository.findByEmail(request.getEmail()) != null)
            throw new RuntimeException("Пользователь уже существует");
        if (!(request.getPassword().equals(request.getConfirmPassword())))
            throw new RuntimeException("Пароли не совпадают");
    }
}
