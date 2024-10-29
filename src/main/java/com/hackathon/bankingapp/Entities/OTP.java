package com.hackathon.bankingapp.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;
}
