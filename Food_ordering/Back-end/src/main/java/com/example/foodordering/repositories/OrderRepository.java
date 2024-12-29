package com.example.foodordering.repositories;

import com.example.foodordering.DTO.OrderedDTO;
import com.example.foodordering.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT new com.example.foodordering.DTO.OrderedDTO(r.tableId.id, m.id,m.name, o.quantity, m.price) FROM Order o " +
            "JOIN o.reservation r " +
            "JOIN o.menuItem m " +
            "WHERE r.tableId.id = :tableId")
    List<OrderedDTO> findByTableId(Long tableId);

    @Query("SELECT new com.example.foodordering.DTO.OrderedDTO(r.tableId.id, m.id, m.name, o.quantity, m.price) FROM Order o " +
            "JOIN o.reservation r " +
            "JOIN o.menuItem m " +
            "WHERE r.id = :reservationId")
    List<OrderedDTO> findOrderedDTOsByReservationId(Long reservationId);


    @Query("SELECT o.reservation.id FROM Order o WHERE o.reservation.tableId.id = :tableID")
    Long findReservationIDsByTableID(Long tableID);



    List<Order> findByReservation_Id(Long reservationID);
}

