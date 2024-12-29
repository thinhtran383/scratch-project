package com.example.foodordering.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRCodeDTO {
    private float amount;
    private String bankAccount;
    private final String bankCode = "MB";
    private String content;
    private final String userBankName = "AB";
    private String sessionId;

    public QRCodeDTO(float amount, String bankAccount, String content, String sessionId) {
        this.amount = amount;
        this.bankAccount = bankAccount;
        this.content = content;
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "\"amount\":" + "\"" + this.getAmount() + "\"," + "\"bankAccount\":" + "\"" + this.getBankAccount() + "\"," + "\"bankCode\":" + "\"" + this.getBankCode() + "\"," + "\"content\":" + "\"" + this.getContent() + "\"," + "\"userBankName\":" + "\""+this.getUserBankName() + "\"";
    }
}
