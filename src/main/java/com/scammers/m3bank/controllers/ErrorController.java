package com.scammers.m3bank.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("error")
public class ErrorController {
    @GetMapping("access-denied-error")
    public String accessError() {
        return "access-denied";
    }
    @GetMapping("resource-not-found")
    public String resourceNotFound() {
        return "not-found";
    }
}
