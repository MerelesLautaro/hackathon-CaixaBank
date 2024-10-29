package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.OTP;
import com.hackathon.bankingapp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOTPRepository extends JpaRepository<OTP,Long> {

    @Query("select fp from OTP fp where fp.otp = ?1 and fp.user = ?2")
    Optional<OTP> findByOtpAndUser(Integer otp, User user);

    @Query("select fp from OTP fp where fp.user = ?1")
    Optional<OTP> findByUser(User user);
}
