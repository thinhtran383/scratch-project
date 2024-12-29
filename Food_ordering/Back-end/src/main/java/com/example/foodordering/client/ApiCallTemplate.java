package com.example.foodordering.client;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@AllArgsConstructor
public class ApiCallTemplate {
    private static final String authorizationHeader = "Basic RU1CUkVUQUlMV0VCOlNEMjM0ZGZnMzQlI0BGR0AzNHNmc2RmNDU4NDNm";

    @NonNull
    private final RestTemplate restTemplate;
    @NonNull
    private final String apiUrl;
    @NonNull
    private final String requestJson;

    public Map<String, String> callApi(List<String> fields, HttpMethod httpMethod) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseData = responseEntity.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseData);

                Map<String, String> resultFields = new HashMap<>();
                for (String field : fields) {
                    JsonNode fieldValue = rootNode.get(field);
                    if (fieldValue != null) {
                        resultFields.put(field, fieldValue.asText());
                    }
                }
                return resultFields;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Err: " + e.getMessage());
        }
        return null;
    }

    public List<Map<String,Object>> callApi(HttpMethod httpMethod) {
        List<Map<String,Object>> result = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseData = responseEntity.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseData);

                Map<String, Object> resultFields = new HashMap<>();
                JsonNode fieldValue = rootNode.get("sessionId");
                JsonNode listAccount = rootNode.get("cust").get("acct_list");

                resultFields.put("sessionId", fieldValue);

                if (listAccount != null && listAccount.isObject()) {
                    Iterator<Map.Entry<String, JsonNode>> iterator = listAccount.fields();
                    List<String> accountBankList = new ArrayList<>();
                    while (iterator.hasNext()) {
                        Map.Entry<String, JsonNode> entry = iterator.next();
                        String key = entry.getKey();
                        accountBankList.add(key);
                        resultFields.put("acct_list", accountBankList);
                    }
                }

                result.add(resultFields);
                return result;

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Err: " + e.getMessage());
        }
        return null;
    }

}






