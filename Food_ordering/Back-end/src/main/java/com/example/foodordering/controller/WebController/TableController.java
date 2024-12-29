package com.example.foodordering.controller.WebController;

import com.example.foodordering.DTO.ResponseObject;
import com.example.foodordering.common.TableStatus;
import com.example.foodordering.services.WebServices.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table")
public class TableController {

    @Autowired
    TableService tableService;
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllTable(@RequestParam(value = "filterByStatus", required = false) TableStatus tableStatus){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Query successfully", tableService.getAllAvailableTables(tableStatus))
        );
    }

    @PutMapping("/updateTableStatus-{id}")
    public ResponseEntity<ResponseObject> updateTableStatus(@PathVariable Long id, @RequestParam(value = "available", required = false) boolean isAvailable){
//        tableService.changeStatusTable(id, isAvailable);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
//                new ResponseObject("ok", "Query successfully","")
//        );
//

        if (tableService.changeStatusTable(id, isAvailable) != 1) { // can toi uu lai
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    new ResponseObject("ok", "Query successfully","")
            );
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    new ResponseObject("ok", "Query successfully", "")
            );
        }

    }


}
