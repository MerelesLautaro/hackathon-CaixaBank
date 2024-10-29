package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.UserDTO;
import com.hackathon.bankingapp.DTO.UserRegisterRequestDTO;

public interface IUserService {
    public UserDTO saveUser(UserRegisterRequestDTO userRegisterRequestDTO);
    public String encriptPassword(String password);
}
