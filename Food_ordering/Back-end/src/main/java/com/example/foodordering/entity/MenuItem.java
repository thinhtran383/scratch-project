package com.example.foodordering.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "MenuItems")
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MenuItemID")
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Price", nullable = false)
    private BigDecimal price;

//
    @Column(name = "ImageUrl", nullable = true)
    private String imageUrl;


}
