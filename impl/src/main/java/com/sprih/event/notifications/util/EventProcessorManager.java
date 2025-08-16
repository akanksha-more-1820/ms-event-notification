package com.sprih.event.notifications.util;

import com.sprih.event.notifications.enums.EventType;
import com.sprih.event.notifications.models.Event;
import com.sprih.event.notifications.service.CallbackClient;
import com.sprih.event.notifications.service.EventProcessor;
import com.sprih.event.notifications.service.RandomFailureDecider;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class EventProcessorManager {

    private final Map<EventType, ExecutorService> executors = new EnumMap<>(EventType.class);
    private final Map<EventType, EventProcessor> processors = new EnumMap<>(EventType.class);
    private final EventQueueManager queueManager;
    private final CallbackClient callbackClient;

    private final int failureRatePercent;
    private final long emailDelayMs;
    private final long smsDelayMs;
    private final long pushDelayMs;

    private volatile boolean shuttingDown = false;

    public EventProcessorManager(EventQueueManager queueManager,
                                 CallbackClient callbackClient,
                                 @Value("${events.failureRatePercent:10}") int failureRatePercent,
                                 @Value("${events.delay.email.ms:5000}") long emailDelayMs,
                                 @Value("${events.delay.sms.ms:3000}") long smsDelayMs,
                                 @Value("${events.delay.push.ms:2000}") long pushDelayMs) {
        this.queueManager = queueManager;
        this.callbackClient = callbackClient;
        this.failureRatePercent = failureRatePercent;
        this.emailDelayMs = emailDelayMs;
        this.smsDelayMs = smsDelayMs;
        this.pushDelayMs = pushDelayMs;
    }

    @PostConstruct
    public void startProcessors() {
        startProcessor(EventType.EMAIL, emailDelayMs);
        startProcessor(EventType.SMS, smsDelayMs);
        startProcessor(EventType.PUSH, pushDelayMs);
    }

    private void startProcessor(EventType type, long delay) {
        BlockingQueue<Event> queue = queueManager.getQueue(type);
        EventProcessor processor = new EventProcessor(queue, delay, callbackClient, new RandomFailureDecider(failureRatePercent));
        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, type.name() + "-processor");
            t.setDaemon(false);
            return t;
        });
        processors.put(type, processor);
        executors.put(type, executor);
        executor.submit(processor);
    }

    public boolean queueEvent(Event event) {
        if (shuttingDown) {
            return false;
        }
        return queueManager.getQueue(event.getEventType()).offer(event);
    }

    @PreDestroy
    public void shutdown() {
        shuttingDown = true;

        // Stop processors
        processors.values().forEach(EventProcessor::stop);

        // Shutdown executors
        executors.values().forEach(ExecutorService::shutdown);

        // Await termination
        for (ExecutorService exec : executors.values()) {
            try {
                if (!exec.awaitTermination(30, TimeUnit.SECONDS)) {
                    exec.shutdownNow();
                }
            } catch (InterruptedException e) {
                exec.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    // For testing purposes
    Map<EventType, ExecutorService> getExecutors() {
        return executors;
    }

    Map<EventType, EventProcessor> getProcessors() {
        return processors;
    }
}