package com.hackathon.bankingapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyAssetRequestDTO {
    private String assetSymbol;
    private double amount;
    private String pin;
}