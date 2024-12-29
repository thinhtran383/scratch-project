package com.example.foodordering.controller.WebController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodordering.DTO.ResponseObject;
import com.example.foodordering.DTO.RevenueDTO;

import com.example.foodordering.services.WebServices.RevenueService;

@RestController
@RequestMapping("/api/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    @Autowired
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getMonthlyRevenue(@RequestParam("year") int year) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query successfully", revenueService.getMonthlyRevenue(year)));
    }

    @GetMapping("/this-week")
    public ResponseEntity<ResponseObject> getLast7DaysRevenue() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query successfully", revenueService.getThisWeekAmount()));
    }
}