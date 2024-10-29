package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.Services.IAssetHoldingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/market")
@PreAuthorize("permitAll()")
public class MarketPriceController {

    private final IAssetHoldingService assetHoldingService;

    public MarketPriceController(IAssetHoldingService assetHoldingService) {
        this.assetHoldingService = assetHoldingService;
    }

    @GetMapping("/prices")
    public ResponseEntity<Map<String, Double>> getAllMarketPrices() {
        try {
            Map<String, Double> prices = assetHoldingService.fetchCurrentMarketPrices();
            return ResponseEntity.ok(prices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyMap());
        }
    }

    @GetMapping("/prices/{symbol}")
    public ResponseEntity<Double> getAssetPrice(@PathVariable String symbol) {
        try {
            double price = assetHoldingService.fetchCurrentAssetPrice(symbol);
            return ResponseEntity.ok(price);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
