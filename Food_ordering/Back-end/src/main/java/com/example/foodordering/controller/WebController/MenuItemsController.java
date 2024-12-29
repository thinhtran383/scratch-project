package com.example.foodordering.controller.WebController;

import com.example.foodordering.DTO.ResponseObject;
import com.example.foodordering.entity.MenuItem;
import com.example.foodordering.services.WebServices.MenuItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("api/menu")
public class MenuItemsController {

    @Autowired
    private MenuItemsService menuItemService;


    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addNewItem(@RequestBody MenuItem menuItem){
        if(!menuItemService.addNewItem(menuItem)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ResponseObject("false", "Item is existed", "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","Query successfully", "")
            );
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllMenu(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Query successfully", menuItemService.getAllMenu())
        );
    }

    @GetMapping("/{id}.jpg") // get image food
    public ResponseEntity<?> getImageById(@PathVariable Long id) throws MalformedURLException {
        Resource image = menuItemService.getImageById(id);
        if(image != null){
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                  new ResponseObject("false", "Image not found", "")
        );
    }

}
