package com.ddasum.domain.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
@Slf4j
public class IamportService {
    @Value("${iamport.api-key}")
    private String apiKey;
    @Value("${iamport.api-secret}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken";
        Map<String, String> hashMap = Map.of("imp_key", apiKey, "imp_secret", apiSecret);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, hashMap, Map.class);
        return (String) ((Map) response.getBody().get("response")).get("access_token");
    }

    public Map verifyPayment(String impUid) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String url = "https://api.iamport.kr/payments/" + impUid;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return (Map) response.getBody().get("response");
    }

    public void cancelPayment(String impUid, int amount, String reason) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        Map<String, Object> hashMap = Map.of("imp_uid", impUid, "amount", amount, "reason", reason);
        HttpEntity<Map<String, Object>> httpHashMap = new HttpEntity<>(hashMap, headers);
        String url = "https://api.iamport.kr/payments/cancel";
        restTemplate.postForEntity(url, httpHashMap, Map.class);
    }
} 