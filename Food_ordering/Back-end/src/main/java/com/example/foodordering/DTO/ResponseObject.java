package com.example.foodordering.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class ResponseObject { // resful api
    private String status;
    private String message;
    private Object data;


}
