package com.sprih.event.notifications.util;

import com.sprih.event.notifications.enums.EventType;
import com.sprih.event.notifications.models.Event;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class EventQueueManager {
    private final Map<EventType, BlockingQueue<Event>> queues = new EnumMap<>(EventType.class);

    public EventQueueManager() {
        for (EventType t : EventType.values()) {
            queues.put(t, new LinkedBlockingQueue<>());
        }
    }

    public BlockingQueue<Event> getQueue(EventType type) {
        return queues.get(type);
    }
}