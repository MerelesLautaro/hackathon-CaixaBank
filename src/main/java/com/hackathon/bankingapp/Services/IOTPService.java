package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.OTPVerificationRequestDTO;
import com.hackathon.bankingapp.DTO.ResetPasswordRequestDTO;
import com.hackathon.bankingapp.DTO.VerifiedEmailDTO;
import com.hackathon.bankingapp.Entities.User;

import java.util.Map;

public interface IOTPService {
    public Map<String, String> verifyEmail(VerifiedEmailDTO verifiedEmailDTO);
    public Map<String, String> verifyOtp(OTPVerificationRequestDTO otpVerificationRequestDTO);
    public Map<String, String> resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO);
    public void deleteOtpIfExists(User user);
}
