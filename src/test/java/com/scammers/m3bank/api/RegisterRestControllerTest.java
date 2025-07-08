package com.scammers.m3bank.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scammers.m3bank.config.ObjectMapperFactory;
import com.scammers.m3bank.controllers.RegisterRestController;
import com.scammers.m3bank.models.dto.UserCreateRequest;
import com.scammers.m3bank.services.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterRestController.class)
public class RegisterRestControllerTest extends BaseTest {
    private final ObjectMapper objectMapper = ObjectMapperFactory.create();
    @MockBean
    private RegistrationService registrationService;

    @Test
    void registerUser_Success() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(registrationService).registerUser(request);
    }

    @Test
    void registerUser_ValidationErrors() throws Exception {
        UserCreateRequest request = new UserCreateRequest();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void registerUser_ServiceThrowsException() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("jane.doe@example.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");

        doThrow(new RuntimeException("Пользователь уже существует"))
                .when(registrationService).registerUser(request);

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Пользователь уже существует"));
    }
}
