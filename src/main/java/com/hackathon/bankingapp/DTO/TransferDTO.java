package com.hackathon.bankingapp.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {
    private String pin;
    private double amount;
    private String targetAccountNumber;
}

