package com.hackathon.bankingapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTPVerificationRequestDTO {
    private String identifier;
    private String otp;
}
