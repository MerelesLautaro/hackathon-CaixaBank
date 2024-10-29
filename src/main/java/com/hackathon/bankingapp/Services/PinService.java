package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.PinDTO;
import com.hackathon.bankingapp.DTO.UpdatePinDTO;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.Pin;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Exceptions.EntityNotFoundException;
import com.hackathon.bankingapp.Repositories.IAccountRepository;
import com.hackathon.bankingapp.Repositories.IPinRepository;
import com.hackathon.bankingapp.Repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class PinService implements IPinService{

    private final IPinRepository pinRepository;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;

    public PinService(IPinRepository pinRepository, IUserRepository userRepository, IAccountRepository accountRepository){
        this.pinRepository = pinRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Map<String, String> savePin(String email, PinDTO pinDTO) {
        User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Account account = user.getAccount();
        Pin newPin = new Pin();
        newPin.setPin(pinDTO.getPin());
        newPin.setPassword(pinDTO.getPassword());

        account.setPin(newPin);
        accountRepository.save(account);
        return Collections.singletonMap("msg","PIN created successfully");
    }

    @Override
    @Transactional
    public Map<String, String> editPin(String email, UpdatePinDTO updatePinDTO) {
        User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Pin existingPin = user.getAccount().getPin();

        if (!existingPin.getPin().equals(updatePinDTO.getOldPin()) ||
                !existingPin.getPassword().equals(updatePinDTO.getPassword())) {
            throw new IllegalArgumentException("Invalid PIN or password");
        }

        existingPin.setPin(updatePinDTO.getNewPin());
        pinRepository.save(existingPin);

        return Collections.singletonMap("msg","PIN updated successfully");
    }
}
