package com.scammers.m3bank.services;

import com.scammers.m3bank.annotations.Auditable;
import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.enums.TransactionType;
import com.scammers.m3bank.models.Account;
import com.scammers.m3bank.models.Notification;
import com.scammers.m3bank.models.Transaction;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.repositories.AccountRepository;
import com.scammers.m3bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final NotificationProducer notificationProducer;

    @Auditable(action = "Создание нового счёта")
    public Account createAccount(Long userId, AccountType accountType, Double balance)
            throws IllegalArgumentException {
        if (userRepository.findById(userId) == null) throw new IllegalArgumentException("Пользователь не авторизован");
        if (balance < 0) throw new IllegalArgumentException("Баланс не может быть отрицательным");

        Account account = new Account(userId, accountType, balance);
        accountRepository.save(account);
        log.info("Создан счёт: {}", account.getAccountUUID());

        Transaction t = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(balance)
                .sourceAccountId(account.getAccountUUID())
                .timestamp(LocalDateTime.now())
                .build();
        transactionService.save(t);
        log.info("Транзакция сохранена: {} руб. → {}", balance, account.getAccountUUID());

        return account;
    }

    @Auditable(action = "Перевод денежных средств")
    public void transferMoney(User sender, String sender_uuid, String receiver_uuid, Double amount)
            throws IllegalArgumentException {
        log.info("Запрос на перевод: от {} к {}, сумма={}, инициатор={}", sender_uuid, receiver_uuid, amount, sender.getEmail());

        if (userRepository.findById(sender.getId()) == null)
            throw new IllegalArgumentException("Пользователь не авторизован");
        if (sender_uuid.equals(receiver_uuid))
            throw new IllegalArgumentException("Нужно выбрать разные счета");

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
        log.info("Баланс обновлён: {} → {}, {} → {}",
                sender_uuid, senderAccount.getBalance(),
                receiver_uuid, receiverAccount.getBalance());

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction t = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .sourceAccountId(sender_uuid)
                .targetAccountId(receiver_uuid)
                .timestamp(LocalDateTime.now())
                .build();
        transactionService.save(t);

        Notification notification = Notification.builder()
                .message("Поступил перевод на сумму " + amount + " руб.")
                .receiverId(receiverAccount.getUserId())
                .senderId(senderAccount.getUserId())
                .sentAt(LocalDateTime.now())
                .received(false)
                .build();
        notificationProducer.send(notification);
        log.info("Уведомление отправлено получателю: userId={}", receiverAccount.getUserId());
    }

    public Account getAccountByUuid(String uuid) {
        return accountRepository.findByUuid(uuid);
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }
}
