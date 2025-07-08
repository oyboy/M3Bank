package com.scammers.m3bank.unit;

import com.scammers.m3bank.models.User;
import com.scammers.m3bank.models.dto.UserCreateRequest;
import com.scammers.m3bank.repositories.UserRepository;
import com.scammers.m3bank.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        registrationService = new RegistrationService(userRepository, passwordEncoder);
    }

    @Test
    void registerUser_shouldSaveUserSuccessfully() {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");

        when(userRepository.findByEmail("john@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");

        registrationService.registerUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("john@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("hashed");
        assertThat(savedUser.getFirstName()).isEqualTo("John");
    }

    @Test
    void registerUser_shouldThrowIfEmailAlreadyExists() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("existing@example.com");
        request.setPassword("pass");
        request.setConfirmPassword("pass");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(new User());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                registrationService.registerUser(request)
        );

        assertThat(exception.getMessage()).isEqualTo("Пользователь уже существует");
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_shouldThrowIfPasswordsDoNotMatch() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("new@example.com");
        request.setPassword("pass1");
        request.setConfirmPassword("pass2");

        when(userRepository.findByEmail("new@example.com")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                registrationService.registerUser(request)
        );

        assertThat(exception.getMessage()).isEqualTo("Пароли не совпадают");
        verify(userRepository, never()).save(any());
    }
}
