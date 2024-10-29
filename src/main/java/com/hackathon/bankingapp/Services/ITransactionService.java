package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.TransactionDTO;
import com.hackathon.bankingapp.Entities.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ITransactionService {
    public Map<String, String> deposit(Long accountId, String pin, double amount);
    public Map<String, String> withdraw(Long accountId, String pin, double amount);
    public Map<String, String> transfer(Long sourceAccountId, String pin, double amount, String targetAccountNumber);
    public List<TransactionDTO> getTransactionHistory(Long accountId);
    public void recordAssetPurchase(Account account, double amount, String assetSymbol);
    public void recordAssetSale(Account account, double quantitySold, String assetSymbol, double gainOrLoss);
}
