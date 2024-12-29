package com.example.foodordering.DTO;

import com.example.foodordering.DTO.BaseDTO.AccountDTO;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@AllArgsConstructor

public class BankAccountDTO extends AccountDTO {
    private String captcha;

    @Override
    public String toString() {
        return "\"userId\":" + "\"" + this.getUsername() + "\"," + "\"password\":" + "\"" + this.getPassword() + "\"," + "\"captcha\":" + "\"" + this.getCaptcha() + "\"";
    }
}
