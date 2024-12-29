package com.example.foodordering.DTO;


import com.example.foodordering.DTO.BaseDTO.AccountDTO;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PasswordForgotDTO extends AccountDTO {
    private String confirmPassword;
}
