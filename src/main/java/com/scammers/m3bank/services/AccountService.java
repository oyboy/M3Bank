package com.scammers.m3bank.services;

import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.models.Account;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.repositories.AccountRepository;
import com.scammers.m3bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public Account createAccount(Long userId, AccountType accountType, Double balance)
        throws IllegalArgumentException {
        if (userRepository.findById(userId) == null) throw new IllegalArgumentException("Пользователь не авторизован");
        if (balance < 0) throw new IllegalArgumentException("Баланс не может быть отрицательным");

        Account account = new Account(userId, accountType, balance);
        accountRepository.save(account);

        log.info("Created account " + account);
        return account;
    }

    public void transferMoney(User sender, String sender_uuid, String receiver_uuid, Double amount)
        throws IllegalArgumentException {
        if (userRepository.findById(sender.getId()) == null)
            throw new IllegalArgumentException("Пользователь не авторизован");

        Account receiverAccount = accountRepository.findByUuid(receiver_uuid);
        if (receiverAccount == null)
            throw new IllegalArgumentException("Счёт получателя не найдн");

        Account senderAccount = accountRepository.findByUuid(sender_uuid);
        if (senderAccount == null || !senderAccount.getUserId().equals(sender.getId()))
            throw new IllegalArgumentException("Отправитель не является владельцем счёта или тот не найден");
        if (senderAccount.isBlocked())
            throw new IllegalArgumentException("Вы не можете отправить деньги с этого счёта, т.к. он заблокирован");

        if (amount <= 0)
            throw new IllegalArgumentException("Сумма для перевода должна быть положительной");

        if (senderAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств");
        }

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        log.info("Transferred account " + senderAccount + " to " + receiverAccount);
    }

    public Account getAccountByUuid(String uuid) {
        return accountRepository.findByUuid(uuid);
    }
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }
}
