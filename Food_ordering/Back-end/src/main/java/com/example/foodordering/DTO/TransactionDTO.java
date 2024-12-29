package com.example.foodordering.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionDTO {
    private LocalDateTime transactionDate;
    private float creditAmount;
    private String description;
}
