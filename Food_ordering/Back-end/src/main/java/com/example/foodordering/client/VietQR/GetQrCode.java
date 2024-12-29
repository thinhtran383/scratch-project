package com.example.foodordering.client.VietQR;


import com.example.foodordering.client.ApiCallTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GetQrCode {
    private static final String apiUrl ="https://api.vietqr.org/vqr/api/qr/generate/unauthenticated";

    private final List<String> fields = new ArrayList<>(List.of("qrCode"));
    
    public Map<String, String> getQrCode(String requestJson){
        RestTemplate restTemplate = new RestTemplate();
        ApiCallTemplate apiCallTemplate = new ApiCallTemplate(restTemplate,apiUrl,getRequestJson(requestJson));
        return apiCallTemplate.callApi(fields, HttpMethod.POST);
    }

    private String getRequestJson(String request) {
        return "{" + request + "}";
    }
}
