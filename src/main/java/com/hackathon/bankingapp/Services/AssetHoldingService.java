package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.AssetHolding;
import com.hackathon.bankingapp.Exceptions.EntityNotFoundException;
import com.hackathon.bankingapp.Repositories.IAccountRepository;
import com.hackathon.bankingapp.Repositories.IAssetHoldingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AssetHoldingService implements IAssetHoldingService{

    @Autowired
    @Qualifier("customRestTemplate")
    private RestTemplate restTemplate;

    private final IAssetHoldingRepository assetHoldingRepository;
    private final IAccountRepository accountRepository;
    private final TransactionService transactionService;
    private final EmailService emailService;

    public AssetHoldingService(IAssetHoldingRepository assetHoldingRepository,
                               IAccountRepository accountRepository, EmailService emailService,
                               TransactionService transactionService){
        this.assetHoldingRepository = assetHoldingRepository;
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.transactionService = transactionService;
    }

    @Override
    @Transactional
    public void buyAsset(Long accountId, String pin, String assetSymbol, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        validatePin(account, pin);

        double currentPrice = fetchCurrentAssetPrice(assetSymbol);
        double quantityPurchased = amount / currentPrice;

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);

        List<AssetHolding> holdings = assetHoldingRepository.findByAssetSymbolAndAccount(assetSymbol, account);
        AssetHolding assetHolding;

        if (holdings.isEmpty()) {
            assetHolding = new AssetHolding(assetSymbol, quantityPurchased, currentPrice, account);
        } else {
            assetHolding = holdings.get(0);
            assetHolding.setQuantity(assetHolding.getQuantity() + quantityPurchased);
        }

        assetHoldingRepository.save(assetHolding);

        transactionService.recordAssetPurchase(account, amount, assetSymbol);

        double netWorth = calculateNetWorth(account.getId());
        emailService.sendPurchaseEmail(account, assetHolding, amount, netWorth);
    }

    @Override
    @Transactional
    public void sellAsset(Long accountId, String pin, String assetSymbol, double quantityToSell) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        validatePin(account, pin);

        List<AssetHolding> holdings = assetHoldingRepository.findByAssetSymbolAndAccount(assetSymbol, account);

        if (holdings.isEmpty()) {
            throw new EntityNotFoundException("Asset not found in holdings");
        }

        AssetHolding holding = holdings.get(0);

        if (holding.getQuantity() < quantityToSell) {
            throw new IllegalArgumentException("Not enough assets to sell");
        }

        double currentPrice = fetchCurrentAssetPrice(assetSymbol);
        double gainOrLoss = (currentPrice - holding.getPurchasePrice()) * quantityToSell;

        account.setBalance(account.getBalance() + (currentPrice * quantityToSell));
        holding.setQuantity(holding.getQuantity() - quantityToSell);

        if (holding.getQuantity() == 0) {
            assetHoldingRepository.delete(holding);
        } else {
            assetHoldingRepository.save(holding);
        }

        transactionService.recordAssetSale(account, quantityToSell, assetSymbol, gainOrLoss);

        double netWorth = calculateNetWorth(account.getId());
        emailService.sendSaleEmail(account, holding, quantityToSell, gainOrLoss, netWorth);
    }

    @Override
    public double fetchCurrentAssetPrice(String assetSymbol) {
        String url = "https://faas-lon1-917a94a7.doserverless.co/api/v1/web/fn-e0f31110-7521-4cb9-86a2-645f66eefb63/default/market-prices-simulator";

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Double> prices = response.getBody();

            if (prices != null && prices.containsKey(assetSymbol)) {
                return prices.get(assetSymbol);
            } else {
                throw new IllegalArgumentException("Asset symbol not found in market prices");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching current asset price: " + e.getMessage(), e);
        }
    }

    @Override
    public double calculateNetWorth(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        double totalAssetsValue = assetHoldingRepository.findByAccount(account).stream()
                .mapToDouble(holding -> holding.getQuantity() * fetchCurrentAssetPrice(holding.getAssetSymbol()))
                .sum();

        return account.getBalance() + totalAssetsValue;
    }

    @Override
    public Map<String, Double> fetchCurrentMarketPrices() {
        String url = "https://faas-lon1-917a94a7.doserverless.co/api/v1/web/fn-e0f31110-7521-4cb9-86a2-645f66eefb63/default/market-prices-simulator";

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching current market prices: " + e.getMessage(), e);
        }
    }
    private void validatePin(Account account, String pin) {
        if (!account.getPin().getPin().equals(pin)) {
            throw new IllegalArgumentException("PIN incorrecto");
        }
    }
}
