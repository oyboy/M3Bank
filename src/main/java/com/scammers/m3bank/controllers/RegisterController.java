package com.scammers.m3bank.controllers;

import com.scammers.m3bank.models.dto.UserCreateRequest;
import com.scammers.m3bank.services.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        Exception exception = (Exception) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception != null) {
            model.addAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);
        }
        return "login";
    }

    @GetMapping("/registration")
    public String register() {
        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(@Valid UserCreateRequest request, Errors errors, Model model) {
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> {
                System.out.println("Error in register_controller: " + error);
            });
            model.addAttribute("errors", errors);
            return "registration";
        }
        try {
            registrationService.registerUser(request);
            return "redirect:/";
        } catch (RuntimeException re) {
            model.addAttribute("userCreateError",
                    re.getMessage());
            re.printStackTrace();
            return "registration";
        }
    }
}
