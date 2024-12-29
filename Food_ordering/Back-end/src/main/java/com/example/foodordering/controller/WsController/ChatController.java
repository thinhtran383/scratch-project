package com.example.foodordering.controller.WsController;

import com.example.foodordering.DTO.BaseDTO.AccountDTO;
import com.example.foodordering.DTO.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
//    @MessageMapping("/hello")
//    @SendTo("/topic/thinh")
//    public ResponseEntity<ResponseObject> greeting(@Payload AccountDTO accountDTO) {
//        System.out.println(accountDTO);
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("ok","ok",accountDTO)
//        );
//
//    }

//    @MessageMapping("/hello")
//    @SendTo("/topic/thinh")
//    public String greeting(String hello) {
//      return "Xin chao" + hello;
//
//    }

//    @MessageMapping("/hello")
//    public void handleClientConnect() {
//        // Gửi thông điệp khi client kết nối
//        messagingTemplate.convertAndSend("/topic/thinh", "Hello thinh!");
//    }

//    public void sendMessageFormServer() throws InterruptedException {
////        Thread.sleep(1000);
//        String message = "Hello from server";
//        messagingTemplate.convertAndSend("/topic/thinh", message);
//
//    }

    @MessageMapping("/hello")
    @SendTo("/topic/thinh")
    public String doubleReturn() throws InterruptedException {
        messagingTemplate.convertAndSend("/topic/thinh","hello one");

        Thread.sleep(2000);
        return "hello two";
    }
}

