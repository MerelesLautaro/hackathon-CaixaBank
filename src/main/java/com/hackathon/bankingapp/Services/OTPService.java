package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.MailBodyDTO;
import com.hackathon.bankingapp.DTO.OTPVerificationRequestDTO;
import com.hackathon.bankingapp.DTO.ResetPasswordRequestDTO;
import com.hackathon.bankingapp.DTO.VerifiedEmailDTO;
import com.hackathon.bankingapp.Entities.OTP;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Exceptions.EntityNotFoundException;
import com.hackathon.bankingapp.Repositories.IOTPRepository;
import com.hackathon.bankingapp.Repositories.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OTPService implements IOTPService {

    private final EmailService emailService;
    private final IOTPRepository iotpRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OTPService(EmailService emailService, IOTPRepository iotpRepository, IUserRepository userRepository, PasswordEncoder passwordEncoder){
        this.emailService = emailService;
        this.iotpRepository =  iotpRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void deleteOtpIfExists(User user) {
        Optional<OTP> existingOtp = iotpRepository.findByUser(user);
        existingOtp.ifPresent(iotpRepository::delete);
    }

    @Override
    public Map<String, String> verifyEmail(VerifiedEmailDTO verifiedEmailDTO) {
        String email = verifiedEmailDTO.getIdentifier();
        User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        // Eliminar OTP existente si hay uno
        deleteOtpIfExists(user);

        int otp = otpGenerator();
        MailBodyDTO mailBody = MailBodyDTO.builder()
                .to(email)
                .text("OTP:" + otp)
                .subject("Your OTP for Password Reset")
                .build();

        OTP newOtp = OTP.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        iotpRepository.save(newOtp);

        return Collections.singletonMap("message", "OTP sent successfully to: "+email);
    }

    @Override
    public Map<String, String> verifyOtp(OTPVerificationRequestDTO otpVerificationRequestDTO) {
        String email = otpVerificationRequestDTO.getIdentifier();
        Integer otp = Integer.valueOf(otpVerificationRequestDTO.getOtp());

        User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        OTP fp = iotpRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            iotpRepository.deleteById(fp.getFpid());
            return Collections.singletonMap("message", "OTP has expired");
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));

        userRepository.save(user);

        return Collections.singletonMap("passwordResetToken",resetToken);
    }

    @Override
    public Map<String, String> resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) {
        String email = resetPasswordRequestDTO.getIdentifier();
        String resetToken = resetPasswordRequestDTO.getResetToken();
        String newPassword = resetPasswordRequestDTO.getNewPassword();

        User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        if (!resetToken.equals(user.getResetToken()) || user.getResetTokenExpiry() == null
                || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return Collections.singletonMap("message","Invalid o expired reset token");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        userRepository.updatePassword(email,encodedPassword);

        user.setPassword(encodedPassword);
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
        return Collections.singletonMap("message","Password reset successfully");
    }

    private Integer otpGenerator(){
        Random random =  new Random();
        return random.nextInt(100_000,999_999);
    }
}