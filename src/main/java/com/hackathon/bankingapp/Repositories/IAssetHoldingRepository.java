package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.AssetHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAssetHoldingRepository extends JpaRepository<AssetHolding,Long> {
    List<AssetHolding> findByAccount(Account account);
    List<AssetHolding> findByAssetSymbolAndAccount(String assetSymbol, Account account);
}
