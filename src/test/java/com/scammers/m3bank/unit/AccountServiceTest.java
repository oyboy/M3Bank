package com.scammers.m3bank.unit;

import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.models.Account;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.repositories.AccountRepository;
import com.scammers.m3bank.repositories.UserRepository;
import com.scammers.m3bank.services.AccountService;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Сервис банковских счетов")
@Feature("Создание и перевод средств между счетами")
@DisplayName("Тесты для сервиса AccountService")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    private User mockUser;
    private Account mockAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1L);

        mockAccount = new Account();
        mockAccount.setUserId(1L);
        mockAccount.setBalance(100.0);
    }

    @Test
    @DisplayName("Создание банковского счета с положительным балансом")
    @Description("Тест проверяет успешное создание банковского счета с положительным балансом.")
    void testCreateAccount_success() {
        when(userRepository.findById(1L)).thenReturn(mockUser);
        doNothing().when(accountRepository).save(any(Account.class));


        Account createdAccount = accountService.createAccount(1L, AccountType.SAVING, 100.0);

        assertNotNull(createdAccount);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Попытка создания банковского счета с несуществующим пользователем")
    @Description("Тест проверяет сценарий, когда попытка создания банковского счета с несуществующим пользователем вызывает ошибку.")
    void testCreateAccount_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(1L, AccountType.SAVING, 100.0);
        });

        assertEquals("Пользователь не авторизован", exception.getMessage());
    }

    @Test
    @DisplayName("Попытка создания банковского счета с отрицательным балансом")
    @Description("Тест проверяет сценарий, когда попытка создать банковский счет с отрицательным балансом вызывает ошибку.")
    void testCreateAccount_negativeBalance() {
        when(userRepository.findById(1L)).thenReturn(mockUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(1L, AccountType.SAVING, -100.0);
        });

        assertEquals("Баланс не может быть отрицательным", exception.getMessage());
    }

    @Test
    @DisplayName("Перевод средств между двумя счетами")
    @Description("Тест проверяет успешный перевод средств между двумя счетами.")
    void testTransferMoney_success() {
        when(userRepository.findById(1L)).thenReturn(mockUser);
        when(accountRepository.findByUuid("uuid123")).thenReturn(mockAccount);

        Account receiverAccount = new Account();
        receiverAccount.setBalance(50.0);

        when(accountRepository.findByUuid("uuid456")).thenReturn(receiverAccount);

        accountService.transferMoney(mockUser, "uuid123", "uuid456", 50.0);

        assertEquals(50.0, mockAccount.getBalance());
        assertEquals(100.0, receiverAccount.getBalance());

        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    @DisplayName("Попытка перевода средств с заблокированного счета")
    @Description("Тест проверяет ситуацию, когда попытка перевода средств с заблокированного счета вызывает ошибку.")
    void testTransferMoney_senderAccountBlocked() {
        when(userRepository.findById(1L)).thenReturn(mockUser);
        when(accountRepository.findByUuid("uuid123")).thenReturn(mockAccount);
        when(accountRepository.findByUuid("uuid456")).thenReturn(new Account());
        mockAccount.setBlocked(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transferMoney(mockUser, "uuid123", "uuid456", 50.0);
        });

        assertEquals("Вы не можете отправить деньги с этого счёта, т.к. он заблокирован", exception.getMessage());
    }

    @Test
    @DisplayName("Попытка перевода средств с недостаточными средствами")
    @Description("Тест проверяет ситуацию, когда попытка перевода средств с недостаточными средствами вызывает ошибку.")
    void testTransferMoney_insufficientFunds() {
        when(userRepository.findById(1L)).thenReturn(mockUser);
        when(accountRepository.findByUuid("uuid123")).thenReturn(mockAccount);

        Account receiverAccount = new Account();
        receiverAccount.setBalance(50.0);

        when(accountRepository.findByUuid("uuid456")).thenReturn(receiverAccount);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transferMoney(mockUser, "uuid123", "uuid456", 150.0);
        });

        assertEquals("Недостаточно средств", exception.getMessage());
    }

    @Test
    @DisplayName("Получение счета по UUID")
    @Description("Тест проверяет корректность работы метода для поиска счета по UUID.")
    void testGetAccountByUuid() {
        when(accountRepository.findByUuid("uuid123")).thenReturn(mockAccount);

        Account foundAccount = accountService.getAccountByUuid("uuid123");

        assertNotNull(foundAccount);
        assertEquals(mockAccount, foundAccount);
    }

    @Test
    @DisplayName("Получение всех счетов пользователя")
    @Description("Тест проверяет метод, который возвращает все счета пользователя.")
    void testGetAccountsByUserId() {
        when(accountRepository.findAllByUserId(1L)).thenReturn(List.of(mockAccount));

        List<Account> accounts = accountService.getAccountsByUserId(1L);

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertEquals(mockAccount, accounts.get(0));
    }
}

