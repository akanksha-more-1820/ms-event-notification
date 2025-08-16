package com.sprih.event.notifications.service;

import com.sprih.event.notifications.models.CallbackPayload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.sprih.event.notifications.Constants.CALLBACK_FAILURE_MSG;
import static com.sprih.event.notifications.Constants.COLON;

@Component
public class CallbackClient {

    private final RestTemplate restTemplate;
    public CallbackClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public void sendCallback(String url, CallbackPayload payload) {
        try {
            restTemplate.postForEntity(url, payload, String.class);
        } catch (RestClientException ex) {
            System.err.println(CALLBACK_FAILURE_MSG + payload.getEventId() + COLON + ex.getMessage());
        }
    }
}