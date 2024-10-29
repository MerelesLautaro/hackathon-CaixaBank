package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.MailBodyDTO;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.AssetHolding;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBodyDTO mailBody){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailBody.to());
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        javaMailSender.send(message);
    }

    public void sendPurchaseEmail(Account account, AssetHolding assetHolding, double amount, double netWorth) {
        String subject = "Investment Purchase Confirmation";
        String to = account.getUser().getEmail();
        String text = createPurchaseEmailContent(account, assetHolding, amount, netWorth);

        MailBodyDTO mailBody = MailBodyDTO.builder()
                .to(to)
                .subject(subject)
                .text(text)
                .build();

        sendSimpleMessage(mailBody);
    }

    public void sendSaleEmail(Account account, AssetHolding holding, double quantityToSell, double gainOrLoss, double netWorth) {
        String subject = "Investment Sale Confirmation";
        String to = account.getUser().getEmail();
        String text = createSaleEmailContent(account, holding, quantityToSell, gainOrLoss, netWorth);

        MailBodyDTO mailBody = MailBodyDTO.builder()
                .to(to)
                .subject(subject)
                .text(text)
                .build();

        sendSimpleMessage(mailBody);
    }

    private String createPurchaseEmailContent(Account account, AssetHolding assetHolding, double amount, double netWorth) {
        return String.format("Dear %s,\n\n" +
                        "You have successfully purchased %.2f units of %s for a total amount of $%.2f.\n\n" +
                        "Current holdings of %s: %.2f units\n\n" +
                        "Summary of current assets:\n- %s: %.2f units purchased at $%.2f\n\n" +
                        "Account Balance: $%.2f\nNet Worth: $%.2f\n\n" +
                        "Thank you for using our investment services.\n\nBest Regards,\nInvestment Management Team",
                account.getUser().getName(),
                assetHolding.getQuantity(),
                assetHolding.getAssetSymbol(),
                amount,
                assetHolding.getAssetSymbol(),
                assetHolding.getQuantity(),
                assetHolding.getAssetSymbol(),
                assetHolding.getQuantity(),
                assetHolding.getPurchasePrice(),
                account.getBalance(),
                netWorth
        );
    }

    private String createSaleEmailContent(Account account, AssetHolding holding, double quantityToSell, double gainOrLoss, double netWorth) {
        return String.format("Dear %s,\n\n" +
                        "You have successfully sold %.2f units of %s.\n\n" +
                        "Total Gain/Loss: $%.2f\n\n" +
                        "Remaining holdings of %s: %.2f units\n\n" +
                        "Summary of current assets:\n- %s: %.2f units purchased at $%.2f\n\n" +
                        "Account Balance: $%.2f\nNet Worth: $%.2f\n\n" +
                        "Thank you for using our investment services.\n\nBest Regards,\nInvestment Management Team",
                account.getUser().getName(),
                quantityToSell,
                holding.getAssetSymbol(),
                gainOrLoss,
                holding.getAssetSymbol(),
                holding.getQuantity(),
                holding.getAssetSymbol(),
                holding.getQuantity(),
                holding.getPurchasePrice(),
                account.getBalance(),
                netWorth
        );
    }
}
