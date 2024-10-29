package com.hackathon.bankingapp.DTO;

import com.hackathon.bankingapp.Security.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDTO {
    @NotEmpty(message = "The name cannot be empty")
    private String name;

    @ValidPassword
    private String password;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "The address cannot be empty")
    private String address;

    @NotEmpty(message = "Phone number can't be empty")
    @Pattern(regexp = "^\\d{9}$", message = "The phone number must be 9 digits long")
    private String phoneNumber;
}
