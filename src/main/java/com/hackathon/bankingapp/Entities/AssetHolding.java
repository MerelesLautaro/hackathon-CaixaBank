package com.hackathon.bankingapp.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssetHolding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String assetSymbol;
    private double quantity;
    private double purchasePrice;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public AssetHolding(String assetSymbol, double quantityPurchased, double currentPrice, Account account) {
        this.assetSymbol = assetSymbol;
        this.quantity = quantityPurchased;
        this.purchasePrice = currentPrice;
        this.account = account;
    }
}
