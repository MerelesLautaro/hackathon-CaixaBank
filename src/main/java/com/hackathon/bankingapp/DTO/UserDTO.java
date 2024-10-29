package com.hackathon.bankingapp.DTO;

import com.hackathon.bankingapp.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String accountNumber;
    private String hashedPassword;

    public static UserDTO fromUser(User user){
        if(user == null){
            return null;
        }

        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getAccountNumber(),
                user.getPassword()
        );
    }
}
