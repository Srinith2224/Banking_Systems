package com.banking.transactions.service;

import com.banking.transactions.dto.TransactionDto;
import com.banking.transactions.entity.Transaction;
import com.banking.transactions.exception.TransactionNotFoundException;
import com.banking.transactions.repository.TransactionRepository;
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
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionDto transactionDto;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionDto = new TransactionDto();
        transactionDto.setAccountId(2002L);
        transactionDto.setType("Deposit");
        transactionDto.setAmount(new BigDecimal("750.00"));
        transactionDto.setStatus("SUCCESS");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccountId(2002L);
        transaction.setType("Deposit");
        transaction.setAmount(new BigDecimal("750.00"));
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus("SUCCESS");
    }

    @Test
    void createTransaction_Success() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDto result = transactionService.createTransaction(transactionDto);

        assertNotNull(result);
        assertEquals(transactionDto.getAccountId(), result.getAccountId());
        assertEquals(transactionDto.getType(), result.getType());
        assertEquals("SUCCESS", result.getStatus());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void getTransactionById_Success() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionDto result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(transaction.getAccountId(), result.getAccountId());
    }

    @Test
    void getTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(1L));
    }

    @Test
    void getAllTransactions_Success() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction));

        List<TransactionDto> results = transactionService.getAllTransactions();

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void updateTransaction_Success() {
        transaction.setStatus("PENDING");
        transactionDto.setStatus("SUCCESS");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDto result = transactionService.updateTransaction(1L, transactionDto);

        assertNotNull(result);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_CompletedTransaction() {
        transaction.setStatus("SUCCESS");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        assertThrows(IllegalStateException.class, 
            () -> transactionService.updateTransaction(1L, transactionDto));
    }

    @Test
    void deleteTransaction_Success() {
        transaction.setStatus("PENDING");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).deleteById(1L);

        assertDoesNotThrow(() -> transactionService.deleteTransaction(1L));
        verify(transactionRepository).deleteById(1L);
    }

    @Test
    void deleteTransaction_CompletedTransaction() {
        transaction.setStatus("SUCCESS");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        assertThrows(IllegalStateException.class, 
            () -> transactionService.deleteTransaction(1L));
    }
}
