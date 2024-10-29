package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.DTO.*;
import com.hackathon.bankingapp.Exceptions.EntityNotFoundException;
import com.hackathon.bankingapp.Repositories.IUserRepository;
import com.hackathon.bankingapp.Services.IAccountService;
import com.hackathon.bankingapp.Services.IAssetHoldingService;
import com.hackathon.bankingapp.Services.IPinService;
import com.hackathon.bankingapp.Services.ITransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private final IAccountService accountService;
    private final IPinService pinService;
    private final IUserRepository userRepository;
    private final ITransactionService transactionService;
    private final IAssetHoldingService assetHoldingService;

    public AccountController(IAccountService accountService, IPinService pinService, IUserRepository iUserRepository,
                             ITransactionService transactionService, IAssetHoldingService assetHoldingService){
        this.accountService = accountService;
        this.pinService = pinService;
        this.userRepository = iUserRepository;
        this.transactionService = transactionService;
        this.assetHoldingService = assetHoldingService;
    }

    @PostMapping("/pin/create")
    public ResponseEntity<Map<String, String>> createPin(@RequestBody PinDTO pinDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(pinService.savePin(email, pinDTO));
    }

    @PutMapping("/pin/update")
    public ResponseEntity<Map<String, String>> updatePin(@RequestBody UpdatePinDTO updatePinDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(pinService.editPin(email, updatePinDTO));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Map<String, String>> deposit(@RequestBody DepositDTO depositDTO) {
        Long accountId = getCurrentUserAccountId();
        return ResponseEntity.ok(transactionService.deposit(accountId, depositDTO.getPin(), depositDTO.getAmount()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Map<String, String>> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        Long accountId = getCurrentUserAccountId();;
        return ResponseEntity.ok(transactionService.withdraw(accountId, withdrawDTO.getPin(), withdrawDTO.getAmount()));
    }

    @PostMapping("/fund-transfer")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody TransferDTO transferDTO) {
        Long accountId = getCurrentUserAccountId();
        return ResponseEntity.ok(transactionService.transfer(accountId, transferDTO.getPin(), transferDTO.getAmount(), transferDTO.getTargetAccountNumber()));
    }

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactionHistory() {
        Long accountId = getCurrentUserAccountId();
        return transactionService.getTransactionHistory(accountId);
    }

    @PostMapping("/buy-asset")
    public ResponseEntity<String> buyAsset(@RequestBody BuyAssetRequestDTO request) {
        try {
            Long accountId = getCurrentUserAccountId();
            assetHoldingService.buyAsset(accountId, request.getPin(), request.getAssetSymbol(), request.getAmount());
            return ResponseEntity.ok("Asset purchase successful.");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("PIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("PIN no válido");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error occurred while purchasing the asset.");
        }
    }

    @PostMapping("/sell-asset")
    public ResponseEntity<String> sellAsset(@RequestBody SellAssetRequestDTO request) {
        try {
            Long accountId = getCurrentUserAccountId();
            assetHoldingService.sellAsset(accountId, request.getPin(), request.getAssetSymbol(), request.getQuantity());
            return ResponseEntity.ok("Asset sale successful.");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("PIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("PIN no válido");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error occurred while selling the asset.");
        }
    }


    private Long getCurrentUserAccountId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserEntityByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found")).getAccount().getId();
    }

}
