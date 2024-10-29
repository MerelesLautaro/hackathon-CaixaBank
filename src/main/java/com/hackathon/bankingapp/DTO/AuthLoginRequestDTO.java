package com.hackathon.bankingapp.DTO;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDTO(@NotBlank String email,
                                  @NotBlank String password) {
}
