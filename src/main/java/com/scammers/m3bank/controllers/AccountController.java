package com.scammers.m3bank.controllers;

import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.models.Account;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.services.AccountService;
import com.scammers.m3bank.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
@SessionAttributes("sender_uuid")
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    @ModelAttribute(name = "user")
    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return userService.getUserById(user.getId());
        }
        return new User();
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal User user, Model model) {
        try {
            List<Account> accounts = accountService.getAccountsByUserId(user.getId());
            model.addAttribute("accounts", accounts);
            return "account/profile";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while fetching accounts");
            return "account/error";
        }
    }

    @GetMapping("/view/{account_uuid}")
    public String viewAccount(@AuthenticationPrincipal User user,
                              @PathVariable String account_uuid,
                              Model model) {
        Account account = accountService.getAccountByUuid(account_uuid);
        if (Objects.isNull(account)) {
            model.addAttribute("error", "Account not found");
            return "errors/not-found";
        }
        if (!Objects.equals(account.getUserId(), user.getId())) {
            model.addAttribute("error", "You are not the owner of this account");
            return "errors/access-denied";
        }
        model.addAttribute("account", account);
        return "account/view";
    }

    @GetMapping("/transfer/{account_uuid}")
    public String transferMoneyPage(@AuthenticationPrincipal User user,
                                    @PathVariable String account_uuid,
                                    Model model) {
        Account account = accountService.getAccountByUuid(account_uuid);
        if (Objects.isNull(account)) {
            model.addAttribute("error", "Account not found");
            return "errors/not-found";
        }
        if (!Objects.equals(account.getUserId(), user.getId())) {
            model.addAttribute("error", "You are not the owner of this account");
            return "errors/access-denied";
        }
        model.addAttribute("sender_uuid", account.getAccountUUID());
        return "account/transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(@AuthenticationPrincipal User user,
                                @RequestParam String sender_uuid,
                                @RequestParam String receiver_uuid,
                                @RequestParam Double amount,
                                Model model) {
        try {
            accountService.transferMoney(user, sender_uuid, receiver_uuid, amount);

            model.addAttribute("amount", amount);
            model.addAttribute("receiver_uuid", receiver_uuid);
            model.addAttribute("currency", "RUB");
            model.addAttribute("sender_uuid", sender_uuid);
            return "account/success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "account/transfer";
        }
    }

    @GetMapping("/create")
    public String createAccountPage(Model model) {
        model.addAttribute("accountTypes", AccountType.values());
        return "account/create";
    }

    @PostMapping("/create")
    public String createAccount(@AuthenticationPrincipal User user,
                                @RequestParam AccountType type,
                                @RequestParam Double balance,
                                Model model) {
        try {
            String uuid = accountService.createAccount(user.getId(), type, balance).getAccountUUID();
            return "redirect:/account/view/" + uuid;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "account/create";
        }
    }
}
