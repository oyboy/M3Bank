package com.scammers.m3bank.controllers;

import com.scammers.m3bank.models.dto.UserCreateRequest;
import com.scammers.m3bank.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RegisterRestController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCreateRequest request, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            errors.getFieldErrors().forEach(e ->
                    errorMap.put(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errorMap);
        }

        try {
            registrationService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
