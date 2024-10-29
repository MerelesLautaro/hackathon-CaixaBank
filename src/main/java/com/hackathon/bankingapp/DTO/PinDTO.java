package com.hackathon.bankingapp.DTO;

import com.hackathon.bankingapp.Entities.Pin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PinDTO {

    private String pin;
    private String password;

    public static PinDTO fromPin(Pin pin){
        if(pin ==  null){
            return null;
        }

        return new PinDTO(
                pin.getPin(),
                pin.getPassword()
        );
    }
}
