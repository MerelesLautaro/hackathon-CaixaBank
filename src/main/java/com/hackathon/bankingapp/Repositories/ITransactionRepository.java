package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findBySourceAccount(Account account);
}
