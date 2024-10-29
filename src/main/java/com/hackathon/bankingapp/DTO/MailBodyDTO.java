package com.hackathon.bankingapp.DTO;

import lombok.Builder;

@Builder
public record MailBodyDTO(String to, String subject, String text) {
}
