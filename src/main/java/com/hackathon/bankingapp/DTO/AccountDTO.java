package com.hackathon.bankingapp.DTO;

import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.Pin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String accountNumber;
    private double balance;
    private PinDTO pin;

    public static AccountDTO fromAccount(Account account){
        if(account == null){
            return null;
        }

        return new AccountDTO(
                account.getAccountNumber(),
                account.getBalance(),
                PinDTO.fromPin(account.getPin())
        );
    }
}
