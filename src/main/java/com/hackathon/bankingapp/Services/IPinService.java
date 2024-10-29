package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.PinDTO;
import com.hackathon.bankingapp.DTO.UpdatePinDTO;

import java.util.Map;

public interface IPinService {
    public Map<String, String> savePin(String email, PinDTO pinDTO);
    public Map<String, String> editPin(String email, UpdatePinDTO updatePinDTO);
}
