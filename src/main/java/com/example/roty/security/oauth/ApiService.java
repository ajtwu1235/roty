//package com.example.roty.security.oauth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class ApiService {
//
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    public ApiService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public String sendPostRequest(String url, Object requestObject) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, headers);
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            return responseEntity.getBody();
//        } else {
//            // Handle error response
//            return "Error";
//        }
//    }
//}