package com.banking.transactions.service;

import com.banking.transactions.dto.TransactionDto;
import com.banking.transactions.entity.Transaction;
import com.banking.transactions.exception.TransactionNotFoundException;
import com.banking.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    /**
     * Initiate a new transaction
     * @param transactionDto Transaction details
     * @return Created transaction
     */
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Transaction transaction = convertToEntity(transactionDto);
        // Set default status to SUCCESS for simplicity
        transaction.setStatus("SUCCESS");
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }
    
    /**
     * Get all transactions
     * @return List of all transactions
     */
    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get transaction by ID
     * @param id Transaction ID
     * @return Transaction details
     */
    public TransactionDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        return convertToDto(transaction);
    }
    
    /**
     * Get transactions by account ID
     * @param accountId Account ID
     * @return List of account transactions
     */
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get transactions by status
     * @param status Transaction status
     * @return List of transactions with the specified status
     */
    public List<TransactionDto> getTransactionsByStatus(String status) {
        return transactionRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update transaction status (for amending transaction details before completion)
     * @param id Transaction ID
     * @param transactionDto Updated transaction details
     * @return Updated transaction
     */
    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        
        // Only allow updates if transaction is still PENDING
        if (!"PENDING".equals(existingTransaction.getStatus())) {
            throw new IllegalStateException("Cannot update completed transaction");
        }
        
        existingTransaction.setAmount(transactionDto.getAmount());
        existingTransaction.setType(transactionDto.getType());
        existingTransaction.setStatus(transactionDto.getStatus());
        
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return convertToDto(updatedTransaction);
    }
    
    /**
     * Cancel a scheduled transaction (delete if PENDING)
     * @param id Transaction ID
     */
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        
        // Only allow deletion if transaction is PENDING
        if (!"PENDING".equals(transaction.getStatus())) {
            throw new IllegalStateException("Cannot cancel completed transaction");
        }
        
        transactionRepository.deleteById(id);
    }
    
    /**
     * Convert Transaction entity to TransactionDto
     */
    private TransactionDto convertToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setAccountId(transaction.getAccountId());
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionDate(transaction.getTransactionDate().toString());
        dto.setStatus(transaction.getStatus());
        return dto;
    }
    
    /**
     * Convert TransactionDto to Transaction entity
     */
    private Transaction convertToEntity(TransactionDto dto) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(dto.getAccountId());
        transaction.setType(dto.getType());
        transaction.setAmount(dto.getAmount());
        return transaction;
    }
}
