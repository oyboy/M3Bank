package com.scammers.m3bank.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/history")
public class TransactionController {
    @GetMapping
    public String transactionHistory() {
        return "transactions";
    }
}
