package com.example.foodordering.entity;

import com.example.foodordering.common.TableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@jakarta.persistence.Table(name = "Tables")
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TableID")
    private Long id;

    @Column(name = "Status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TableStatus status;

}
