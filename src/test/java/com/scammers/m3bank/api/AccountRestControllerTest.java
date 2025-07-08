package com.scammers.m3bank.api;

import com.scammers.m3bank.controllers.AccountRestController;
import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.services.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountRestController.class)
public class AccountRestControllerTest extends BaseTest {
    @MockBean
    private AccountService accountService;

    @Test
    void testCreateAccountSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/account/create")
                        .with(csrf())
                        .param("type", "DEBIT")
                        .param("balance", "1000.0"))
                .andExpect(status().isCreated());

        verify(accountService).createAccount(anyLong(), eq(AccountType.DEBIT), eq(1000.0));
    }

    @Test
    void testCreateAccountInvalidInput() throws Exception {
        doThrow(new IllegalArgumentException("Invalid balance"))
                .when(accountService)
                .createAccount(anyLong(), eq(AccountType.SAVING), eq(-100.0));

        mockMvc.perform(post("/api/v1/account/create")
                        .with(csrf())
                        .param("type", "SAVING")
                        .param("balance", "-100.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid balance"));
    }

    @Test
    void testTransferMoneySuccess() throws Exception {
        mockMvc.perform(post("/api/v1/account/transfer")
                        .with(csrf())
                        .param("sender_uuid", "uuid1")
                        .param("receiver_uuid", "uuid2")
                        .param("amount", "500.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));

        verify(accountService).transferMoney(any(User.class), eq("uuid1"), eq("uuid2"), eq(500.0));
    }

    @Test
    void testTransferMoneyInvalid() throws Exception {
        doThrow(new IllegalArgumentException("Insufficient funds"))
                .when(accountService)
                .transferMoney(any(User.class), eq("uuid1"), eq("uuid2"), eq(99999.0));

        mockMvc.perform(post("/api/v1/account/transfer")
                        .with(csrf())
                        .param("sender_uuid", "uuid1")
                        .param("receiver_uuid", "uuid2")
                        .param("amount", "99999.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds"));
    }
}
