package com.hackathon.bankingapp.Services;

import java.util.Map;

public interface IAssetHoldingService {
    public void buyAsset(Long accountId, String pin, String assetSymbol, double amount);
    public void sellAsset(Long accountId, String pin, String assetSymbol, double quantityToSell);
    public double fetchCurrentAssetPrice(String assetSymbol);
    public double calculateNetWorth(Long accountId);
    public Map<String, Double> fetchCurrentMarketPrices();
}
