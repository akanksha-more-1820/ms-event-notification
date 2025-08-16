package com.sprih.event.notifications.models;

import com.sprih.event.notifications.enums.EventType;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class Event {
    private final String eventId;
    private final EventType eventType;
    private final Map<String, Object> payload;
    private final String callbackUrl;
    private final Instant receivedAt;

    public Event(EventType eventType, Map<String, Object> payload, String callbackUrl) {
        this.eventId = "e-" + UUID.randomUUID();
        this.eventType = eventType;
        this.payload = payload;
        this.callbackUrl = callbackUrl;
        this.receivedAt = Instant.now();
    }

    public String getEventId() { return eventId; }
    public EventType getEventType() { return eventType; }
    public Map<String, Object> getPayload() { return payload; }
    public String getCallbackUrl() { return callbackUrl; }
    public Instant getReceivedAt() { return receivedAt; }
}