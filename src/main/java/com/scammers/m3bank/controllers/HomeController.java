package com.scammers.m3bank.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping
    public String home() {
        return "home";
    }
}
