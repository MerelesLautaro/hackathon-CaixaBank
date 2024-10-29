package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserEntityByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User user set user.password = ?2 where user.email = ?1")
    void updatePassword(String email, String password);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}
