package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.TransactionDTO;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.Transaction;
import com.hackathon.bankingapp.Entities.TransactionType;
import com.hackathon.bankingapp.Exceptions.EntityNotFoundException;
import com.hackathon.bankingapp.Repositories.IAccountRepository;
import com.hackathon.bankingapp.Repositories.ITransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {

    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;

    public TransactionService(IAccountRepository accountRepository,ITransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Map<String, String> deposit(Long accountId, String pin, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        validatePin(account, pin);

        account.setBalance(account.getBalance() + amount);
        transactionRepository.save(new Transaction(amount, LocalDateTime.now(), TransactionType.CASH_DEPOSIT, account, "N/A"));
        accountRepository.save(account);

        return Collections.singletonMap("msg","Cash deposited successfully");
    }

    @Override
    @Transactional
    public Map<String, String> withdraw(Long accountId, String pin, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        validatePin(account, pin);
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        account.setBalance(account.getBalance() - amount);
        transactionRepository.save(new Transaction(amount, LocalDateTime.now(), TransactionType.CASH_WITHDRAWAL, account, "N/A"));
        accountRepository.save(account);

        return Collections.singletonMap("msg","Cash withdrawn successfully");
    }

    @Override
    @Transactional
    public Map<String, String> transfer(Long sourceAccountId, String pin, double amount, String targetAccountNumber) {
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Source account not found"));

        validatePin(sourceAccount, pin);

        if (sourceAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Target account not found"));

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        transactionRepository.save(new Transaction(amount, LocalDateTime.now(), TransactionType.CASH_TRANSFER, sourceAccount, targetAccountNumber));
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return Collections.singletonMap("msg","Fund transferred successfully");
    }

    @Override
    public void recordAssetPurchase(Account account, double amount, String assetSymbol) {
        Transaction transaction = new Transaction(
                amount,
                LocalDateTime.now(),
                TransactionType.ASSET_PURCHASE,
                account,
                assetSymbol
        );
        transactionRepository.save(transaction);
    }

    @Override
    public void recordAssetSale(Account account, double quantitySold, String assetSymbol, double gainOrLoss) {
        Transaction transaction = new Transaction(
                gainOrLoss,
                LocalDateTime.now(),
                TransactionType.ASSET_SELL,
                account,
                assetSymbol
        );
        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDTO> getTransactionHistory(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        List<Transaction> transactions = transactionRepository.findBySourceAccount(account);
        return transactions.stream()
                .map(transaction -> new TransactionDTO(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getTransactionDate(),
                        transaction.getTransactionType().toString(),
                        transaction.getSourceAccount().getAccountNumber(),
                        transaction.getTargetAccountNumber()))
                .collect(Collectors.toList());
    }

    private void validatePin(Account account, String pin) {
        if (!account.getPin().getPin().equals(pin)) {
            throw new IllegalArgumentException("PIN incorrecto");
        }
    }
}
