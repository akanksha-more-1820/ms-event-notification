package com.sprih.event.notifications.api.request;

import com.sprih.event.notifications.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    @NotNull
    private EventType eventType;
    @NotNull
    private Map<String, Object> payload;
    @NotBlank
    private String callbackUrl;
}