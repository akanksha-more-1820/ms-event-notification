package com.sprih.event.notifications.service;

import com.sprih.event.notifications.EventService;
import com.sprih.event.notifications.api.request.EventRequest;
import com.sprih.event.notifications.api.response.EventResponse;
import com.sprih.event.notifications.models.Event;
import com.sprih.event.notifications.util.EventProcessorManager;
import com.sprih.event.notifications.validator.EventRequestValidator;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.sprih.event.notifications.Constants.EVENT_ACCEPTED_MESSAGE;
import static com.sprih.event.notifications.Constants.SYSTEM_SHUTDOWN_MESSAGE;

@Service
public class EventServiceImpl implements EventService {

    private final EventRequestValidator validator;
    private final EventProcessorManager processorManager;
    private final AtomicBoolean accepting = new AtomicBoolean(true);

    public EventServiceImpl(EventRequestValidator validator, EventProcessorManager processorManager) {
        this.validator = validator;
        this.processorManager = processorManager;
    }

    @Override
    public EventResponse createEvent(EventRequest request) {
        if (!accepting.get()) {
            throw new IllegalStateException(SYSTEM_SHUTDOWN_MESSAGE);
        }
        validator.validate(request);
        Event event = new Event(request.getEventType(), request.getPayload(), request.getCallbackUrl());
        processorManager.queueEvent(event);
        return new EventResponse(event.getEventId(), EVENT_ACCEPTED_MESSAGE);
    }

    public void stopAccepting() {
        accepting.set(false);
    }

    boolean isAccepting() {
        return accepting.get();
    }
}