package com.example.foodordering.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Revenues")
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RevenueID")
    private Long id;

    @Column(name = "Date", nullable = false)
    private LocalDateTime date;

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;
}
