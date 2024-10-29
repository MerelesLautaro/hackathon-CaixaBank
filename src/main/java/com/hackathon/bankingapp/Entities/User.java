package com.hackathon.bankingapp.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hackathon.bankingapp.Security.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The name cannot be empty")
    private String name;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Phone number can't be empty")
    @Pattern(regexp = "^\\d{9}$", message = "The phone number must be 9 digits long")
    private String phoneNumber;

    @NotEmpty(message = "The address cannot be empty")
    private String address;

    @ValidPassword
    private String password;

    @Column(unique = true)
    private String accountNumber;

    @PrePersist
    public void generateAccountNumber() {
        this.accountNumber = UUID.randomUUID().toString();
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonManagedReference
    private Account account;

    @OneToOne(mappedBy = "user")
    private OTP otp;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    @NotEmpty(message = "Role list must not be empty")
    private Set<Role> roleList = new HashSet<>();

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    private boolean enabled;
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialNotExpired;

}
