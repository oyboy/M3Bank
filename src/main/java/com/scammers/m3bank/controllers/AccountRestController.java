package com.scammers.m3bank.controllers;

import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountRestController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@AuthenticationPrincipal User user,
                                                @RequestParam AccountType type,
                                                @RequestParam Double balance) {
        try{
            accountService.createAccount(user.getId(), type, balance);
            return ResponseEntity.ok("Account created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@AuthenticationPrincipal User sender,
                                                @RequestParam String sender_uuid,
                                                @RequestParam String receiver_uuid,
                                                @RequestParam Double amount) {
        try{
            accountService.transferMoney(sender, sender_uuid, receiver_uuid, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException re) {
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }
}
