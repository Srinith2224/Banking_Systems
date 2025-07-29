package com.banking.accounts.service;

import com.banking.accounts.dto.AccountDto;
import com.banking.accounts.entity.Account;
import com.banking.accounts.exception.AccountNotFoundException;
import com.banking.accounts.exception.DuplicateAccountException;
import com.banking.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    /**
     * Create a new account
     * @param accountDto Account details
     * @return Created account
     */
    public AccountDto createAccount(AccountDto accountDto) {
        if (accountRepository.existsByAccountNumber(accountDto.getAccountNumber())) {
            throw new DuplicateAccountException("Account number already exists: " + accountDto.getAccountNumber());
        }
        
        Account account = convertToEntity(accountDto);
        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
    }
    
    /**
     * Get all accounts
     * @return List of all accounts
     */
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get account by ID
     * @param id Account ID
     * @return Account details
     */
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        return convertToDto(account);
    }
    
    /**
     * Get accounts by customer ID
     * @param customerId Customer ID
     * @return List of customer accounts
     */
    public List<AccountDto> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update account details
     * @param id Account ID
     * @param accountDto Updated account details
     * @return Updated account
     */
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        
        // Check if account number is being changed and if it already exists
        if (!existingAccount.getAccountNumber().equals(accountDto.getAccountNumber()) &&
            accountRepository.existsByAccountNumber(accountDto.getAccountNumber())) {
            throw new DuplicateAccountException("Account number already exists: " + accountDto.getAccountNumber());
        }
        
        existingAccount.setAccountNumber(accountDto.getAccountNumber());
        existingAccount.setType(accountDto.getType());
        existingAccount.setBalance(accountDto.getBalance());
        
        Account updatedAccount = accountRepository.save(existingAccount);
        return convertToDto(updatedAccount);
    }
    
    /**
     * Delete account by ID
     * @param id Account ID
     */
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }
    
    /**
     * Convert Account entity to AccountDto
     */
    private AccountDto convertToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCustomerId(account.getCustomerId());
        dto.setType(account.getType());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt().toString());
        return dto;
    }
    
    /**
     * Convert AccountDto to Account entity
     */
    private Account convertToEntity(AccountDto dto) {
        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setCustomerId(dto.getCustomerId());
        account.setType(dto.getType());
        account.setBalance(dto.getBalance());
        return account;
    }
}
