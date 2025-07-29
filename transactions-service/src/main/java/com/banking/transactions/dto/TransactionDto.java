package com.banking.transactions.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    
    private Long id;
    
    @NotNull(message = "Account ID is required")
    @Positive(message = "Account ID must be positive")
    private Long accountId;
    
    @NotBlank(message = "Transaction type is required")
    @Pattern(regexp = "^(Deposit|Withdrawal|Transfer)$", message = "Transaction type must be Deposit, Withdrawal, or Transfer")
    private String type;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;
    
    private String transactionDate;
    
    @Pattern(regexp = "^(SUCCESS|FAILED|PENDING)$", message = "Status must be SUCCESS, FAILED, or PENDING")
    private String status;
}
