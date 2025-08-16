package com.sprih.event.notifications.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprih.event.notifications.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallbackPayload {
    private String eventId;
    private String status;
    private EventType eventType;
    private String errorMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant processedAt;
}