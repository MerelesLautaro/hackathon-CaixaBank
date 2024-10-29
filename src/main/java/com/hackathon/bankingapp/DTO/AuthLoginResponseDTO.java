package com.hackathon.bankingapp.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"JWT"})
public record AuthLoginResponseDTO(String JWT) {
}
