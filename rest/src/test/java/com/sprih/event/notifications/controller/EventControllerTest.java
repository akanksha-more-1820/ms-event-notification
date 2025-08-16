package com.sprih.event.notifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprih.event.notifications.EventService;
import com.sprih.event.notifications.api.request.EventRequest;
import com.sprih.event.notifications.enums.EventType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EventController eventController;

    @Mock
    EventService eventService;


    private ObjectMapper objectMapper = new ObjectMapper();

    private EventRequest request;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Hello");
        request = new EventRequest(EventType.EMAIL, payload, "http://callback.url");
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void testCreateEvent_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/events").content(objectMapper.
                writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void testCreateEvent_Failure_MissingRequiredFields() throws Exception {
        String invalidJson = "{\"payload\": {\"message\": \"Hello\"}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                        .content(invalidJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}