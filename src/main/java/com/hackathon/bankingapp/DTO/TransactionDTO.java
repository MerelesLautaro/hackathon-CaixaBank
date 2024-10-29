package com.hackathon.bankingapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private double amount;
    private LocalDateTime transactionDate;
    private String transactionType;
    private String sourceAccountNumber;
    private String targetAccountNumber;
}
