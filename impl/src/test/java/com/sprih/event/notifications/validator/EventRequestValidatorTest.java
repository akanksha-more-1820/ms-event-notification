package com.sprih.event.notifications.validator;

import com.sprih.event.notifications.api.request.EventRequest;
import com.sprih.event.notifications.enums.EventType;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
public class EventRequestValidatorTest {

    private EventRequestValidator validator;

    @Before
    public void setUp() {
        validator = new EventRequestValidator();
    }

    @Test
    public void testValidEmailRequest() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "user@example.com");
        payload.put("message", "Hello!");

        EventRequest request = new EventRequest(EventType.EMAIL, payload, "http://callback.url");

        // Should not throw exception
        validator.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmailRequest_MissingRecipient() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Hello!");

        EventRequest request = new EventRequest(EventType.EMAIL, payload, "http://callback.url");
        validator.validate(request);
    }

    @Test
    public void testValidSmsRequest() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("phoneNumber", "1234567890");
        payload.put("message", "Hello!");

        EventRequest request = new EventRequest(EventType.SMS, payload, "http://callback.url");
        validator.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSmsRequest_MissingPhoneNumber() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Hello!");

        EventRequest request = new EventRequest(EventType.SMS, payload, "http://callback.url");
        validator.validate(request);
    }

    @Test
    public void testValidPushRequest() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("deviceId", "device-123");
        payload.put("message", "Hello!");

        EventRequest request = new EventRequest(EventType.PUSH, payload, "http://callback.url");
        validator.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPushRequest_MissingDeviceId() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Hello!");

        EventRequest request = new EventRequest(EventType.PUSH, payload, "http://callback.url");
        validator.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPayload() {
        EventRequest request = new EventRequest(EventType.EMAIL, null, "http://callback.url");
        validator.validate(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEventType() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Hello!");

        EventRequest request = new EventRequest(null, payload, "http://callback.url");
        validator.validate(request);
    }
}