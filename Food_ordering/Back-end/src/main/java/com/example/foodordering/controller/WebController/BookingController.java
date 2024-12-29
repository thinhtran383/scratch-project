package com.example.foodordering.controller.WebController;

import com.example.foodordering.entity.Table;
import com.example.foodordering.services.WebServices.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodordering.DTO.ResponseObject;
import com.example.foodordering.common.ReservationStatus;
import com.example.foodordering.entity.Reservation;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
  @Autowired
  private BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> createReservation(@RequestBody Reservation reservation) {
      if(bookingService.createReservation(reservation)){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("ok","Create reservation successfully","")
        );
      }
      return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
              new ResponseObject("false","Cannot create reservation successfully","")
      );
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getCustomerAndReservationTime(@RequestParam(value = "filterByStatus", required = false) ReservationStatus filterByStatus){
      return ResponseEntity.status(HttpStatus.OK).body(
              new ResponseObject("ok", "Query successfully", bookingService.getCustomerAndReservationTime(filterByStatus))
      );
    }

    @PutMapping("/reservationId-{reservationId}")
    public ResponseEntity<ResponseObject> setTable(@PathVariable Long reservationId, @RequestBody Table table, @RequestParam(value = "delete", required = false) boolean delete){
      if(bookingService.setTable(reservationId, table, delete) == 1){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ResponseObject("ok","Query successfully", "")
        );
      }

      if(bookingService.setTable(reservationId,table,delete) == 2){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ResponseObject("ok", "Delete successfully", "")
        );
      }

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
              new ResponseObject("false", "ReservationId not found","")
      );

    }

}
