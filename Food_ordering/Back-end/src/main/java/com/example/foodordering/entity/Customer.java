package com.example.foodordering.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Customers")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Long id;
    @Column(name = "Phone", nullable = false)
    private String phone;
    @Column(name = "Name", nullable = false)
    private String name;
}
