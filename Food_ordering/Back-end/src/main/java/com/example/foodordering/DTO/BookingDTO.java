package com.example.foodordering.DTO;

import com.example.foodordering.common.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString


public class BookingDTO {
    private Long id;
    private String name;
    private String phone;
    private Long tableId;
    private LocalDateTime reservationTime;
    private ReservationStatus status;   
}
