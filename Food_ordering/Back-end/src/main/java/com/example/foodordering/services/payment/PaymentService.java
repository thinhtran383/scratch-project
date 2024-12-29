package com.example.foodordering.services.payment;

import com.example.foodordering.client.MbBank.TransactionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private TransactionHistory transactionHistory;

    public boolean TrackingSuccess(String sessionId, float credit, String content, String bankName, String accountNo){
        while(true){
//            System.out.println(content);
            if (transactionHistory.callApi(sessionId, credit, content, bankName, accountNo)){
                break;
            }
        }
        return true;
    }
}
