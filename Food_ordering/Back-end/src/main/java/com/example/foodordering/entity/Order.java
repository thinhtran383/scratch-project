package com.example.foodordering.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Orders")
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Long id;

    @ManyToOne
    @JoinColumn (name = "ReservationID")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "MenuItemID")
    private MenuItem menuItem;

    @Column(name = "Quantity")
    private int quantity;

}
