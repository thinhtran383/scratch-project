package com.example.foodordering.controller.WsController;

import com.example.foodordering.DTO.QRCodeDTO;
import com.example.foodordering.DTO.ResponseObject;
import com.example.foodordering.client.VietQR.GetQrCode;
import com.example.foodordering.controller.WebController.LoginBankController;
import com.example.foodordering.services.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Controller
public class PaymentController {
    @Autowired
    private LoginBankController loginBankController;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PaymentController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    private GetQrCode getQrCode;
    @Autowired
    private PaymentService paymentService;

    // private String sessionId = "86152cf9-4d58-45ad-bc0b-f1c8c8617ff8";

    @MessageMapping("payment")
    @SendTo("/topic/result")
    public ResponseObject HandlerPayment(QRCodeDTO qrCodeDTO) {
        System.out.println(qrCodeDTO.toString());
        System.out.println(qrCodeDTO);
        System.out.println(qrCodeDTO.getSessionId());
        messagingTemplate.convertAndSend("/topic/result", getQrCode.getQrCode(qrCodeDTO.toString()));
        System.out.println(getQrCode.getQrCode(qrCodeDTO.toString()));
        float totalMoney = qrCodeDTO.getAmount();
        CompletableFuture<Void> trackingFuture = CompletableFuture.runAsync(() -> {
            boolean conditionMet = false;
            while (!conditionMet) {
                System.out.println("run");

                conditionMet = paymentService.TrackingSuccess(qrCodeDTO.getSessionId(), totalMoney, "",
                        LoginBankController.bankNameHover, qrCodeDTO.getBankAccount());

                try {
                    Thread.sleep(2000); // Chờ 1 giây trước khi kiểm tra lại
                } catch (InterruptedException e) {
                    // Xử lý ngoại lệ nếu cần thiết
                }
            }
        });

        trackingFuture.join(); // Đợi cho đến khi công việc trong trackingFuture hoàn thành
        return new ResponseObject("ok", "Payment successfully", "");
    }

}
