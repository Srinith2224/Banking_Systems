package com.banking.accounts.service;

import com.banking.accounts.dto.AccountDto;
import com.banking.accounts.entity.Account;
import com.banking.accounts.exception.AccountNotFoundException;
import com.banking.accounts.exception.DuplicateAccountException;
import com.banking.accounts.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountDto accountDto;
    private Account account;

    @BeforeEach
    void setUp() {
        accountDto = new AccountDto();
        accountDto.setAccountNumber("NL91ABNA0417164300");
        accountDto.setCustomerId(1001L);
        accountDto.setType("Savings");
        accountDto.setBalance(new BigDecimal("5000.00"));

        account = new Account();
        account.setId(1L);
        account.setAccountNumber("NL91ABNA0417164300");
        account.setCustomerId(1001L);
        account.setType("Savings");
        account.setBalance(new BigDecimal("5000.00"));
        account.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createAccount_Success() {
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDto result = accountService.createAccount(accountDto);

        assertNotNull(result);
        assertEquals(accountDto.getAccountNumber(), result.getAccountNumber());
        assertEquals(accountDto.getCustomerId(), result.getCustomerId());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_DuplicateAccountNumber() {
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(true);

        assertThrows(DuplicateAccountException.class, () -> accountService.createAccount(accountDto));
    }

    @Test
    void getAccountById_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountDto result = accountService.getAccountById(1L);

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.getAccountNumber());
    }

    @Test
    void getAccountById_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    void getAllAccounts_Success() {
        when(accountRepository.findAll()).thenReturn(Arrays.asList(account));

        List<AccountDto> results = accountService.getAllAccounts();

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void deleteAccount_Success() {
        when(accountRepository.existsById(1L)).thenReturn(true);
        doNothing().when(accountRepository).deleteById(1L);

        assertDoesNotThrow(() -> accountService.deleteAccount(1L));
        verify(accountRepository).deleteById(1L);
    }

    @Test
    void deleteAccount_NotFound() {
        when(accountRepository.existsById(1L)).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(1L));
    }
}
