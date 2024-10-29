package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.DTO.*;
import com.hackathon.bankingapp.Services.IOTPService;
import com.hackathon.bankingapp.Services.OTPService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class OTPController {

    private final IOTPService otpService;

    public OTPController( OTPService otpService){
        this.otpService = otpService;

    }

    @PostMapping("/password-reset/send-otp")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestBody VerifiedEmailDTO verifiedEmailDTO) {
        return ResponseEntity.ok(otpService.verifyEmail(verifiedEmailDTO));
    }

    @PostMapping("/password-reset/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody OTPVerificationRequestDTO otpVerificationRequestDTO) {
        return ResponseEntity.ok(otpService.verifyOtp(otpVerificationRequestDTO));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
        return ResponseEntity.ok(otpService.resetPassword(resetPasswordRequestDTO));
    }
}
