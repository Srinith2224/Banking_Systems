package com.banking.transactions.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_id", nullable = false)
    @NotNull(message = "Account ID is required")
    @Positive(message = "Account ID must be positive")
    private Long accountId;
    
    @Column(name = "type", nullable = false, length = 50)
    @NotBlank(message = "Transaction type is required")
    @Pattern(regexp = "^(Deposit|Withdrawal|Transfer)$", message = "Transaction type must be Deposit, Withdrawal, or Transfer")
    private String type;
    
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
    
    @Column(name = "status", nullable = false, length = 50)
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(SUCCESS|FAILED|PENDING)$", message = "Status must be SUCCESS, FAILED, or PENDING")
    private String status;
    
    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }
}
