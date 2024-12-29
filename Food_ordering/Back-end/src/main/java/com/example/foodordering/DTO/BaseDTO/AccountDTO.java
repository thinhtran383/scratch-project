package com.example.foodordering.DTO.BaseDTO;

import com.example.foodordering.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class AccountDTO {
    private String username;
    private String password;
}
