package com.sprih.event.notifications.service;

import com.sprih.event.notifications.api.request.EventRequest;
import com.sprih.event.notifications.api.response.EventResponse;
import com.sprih.event.notifications.enums.EventType;
import com.sprih.event.notifications.models.Event;
import com.sprih.event.notifications.util.EventProcessorManager;
import com.sprih.event.notifications.validator.EventRequestValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventServiceImplTest {

    private EventRequestValidator validator;
    private EventProcessorManager processorManager;
    private EventServiceImpl service;

    @Before
    public void setUp() {
        validator = mock(EventRequestValidator.class);
        processorManager = mock(EventProcessorManager.class);
        service = new EventServiceImpl(validator, processorManager);
    }

    @Test
    public void testCreateEvent_successful() {
        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);
        request.setCallbackUrl("http://example.com/callback");

        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Hello");
        request.setPayload(payload);

        EventResponse response = service.createEvent(request);

        assertNotNull(response);
        assertNotNull(response.getEventId());
        assertEquals("Event accepted for processing.", response.getMessage());

        verify(validator).validate(request);
        verify(processorManager).queueEvent(any(Event.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateEvent_whenNotAccepting_shouldThrowException() {
        service.stopAccepting();

        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);
        request.setCallbackUrl("http://example.com");
        request.setPayload(new HashMap<>());

        service.createEvent(request);
    }

    @Test
    public void testIsAccepting() {
        assertTrue(service.isAccepting());
        service.stopAccepting();
        assertFalse(service.isAccepting());
    }
}