package com.hackathon.bankingapp.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account sourceAccount;

    @Column(nullable = true)
    private String targetAccountNumber;

    public Transaction(double amount, LocalDateTime transactionDate, TransactionType transactionType, Account sourceAccount, String targetAccountNumber) {
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.sourceAccount = sourceAccount;
        this.targetAccountNumber = targetAccountNumber;
    }
}
