package com.sprih.event.notifications.service;

import com.sprih.event.notifications.enums.EventType;
import com.sprih.event.notifications.models.CallbackPayload;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import static org.mockito.Mockito.*;

public class CallbackClientTest {

    @InjectMocks
    private CallbackClient callbackClient;

    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        callbackClient = new CallbackClient(restTemplate);
    }

    @Test
    public void sendCallback_shouldPostToGivenUrl() {
        String url = "http://example.com/callback";
        CallbackPayload payload = new CallbackPayload("event123", "SUCCESS", EventType.EMAIL,
                "", Instant.now());

        when(restTemplate.postForEntity(eq(url), eq(payload), eq(String.class)))
                .thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));

        callbackClient.sendCallback(url, payload);

        verify(restTemplate, times(1))
                .postForEntity(eq(url), eq(payload), eq(String.class));
    }

    @Test
    public void testSendCallback_shouldHandleRestClientExceptionGracefully() {
        // Arrange
        String url = "http://bad-url.com";
        CallbackPayload payload = new CallbackPayload("event456", "FAILED", EventType.PUSH, "", Instant.now());

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RestClientException("Connection refused"));

        try {
            callbackClient.sendCallback(url, payload);
        } catch (Exception e) {
            org.junit.Assert.fail("sendCallback() should not throw an exception, but threw: " + e);
        }
    }

}