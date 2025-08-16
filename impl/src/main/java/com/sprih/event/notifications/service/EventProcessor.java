package com.sprih.event.notifications.service;

import com.sprih.event.notifications.FailureDecider;
import com.sprih.event.notifications.models.CallbackPayload;
import com.sprih.event.notifications.models.Event;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;

import static com.sprih.event.notifications.Constants.*;

public class EventProcessor implements Runnable {
    private final BlockingQueue<Event> queue;
    private final FailureDecider failureDecider;
    private final CallbackClient callbackClient;
    private final long processingDelayMs;


    private volatile boolean running = true;

    public EventProcessor(BlockingQueue<Event> queue,
                          long processingDelayMs,
                          CallbackClient callbackClient,
                          FailureDecider failureDecider) {
        this.queue = queue;
        this.processingDelayMs = processingDelayMs;
        this.callbackClient = callbackClient;
        this.failureDecider = failureDecider;
    }

    @Override
    public void run() {
        while (running || !queue.isEmpty()) {
            try {
                Event event = queue.poll();
                if (event == null) {
                    Thread.sleep(10);
                    continue;
                }
                // Simulate work
                Thread.sleep(processingDelayMs);

                boolean failed = failureDecider.shouldFail();
                CallbackPayload payload;
                if (failed) {
                    payload = new CallbackPayload(event.getEventId(), FAILED_STATUS, event.getEventType(),
                            SIMULATED_PROCESS_FAILURE_MESSAGE
                            , Instant.now());
                } else {
                    payload = new CallbackPayload(event.getEventId(), COMPLETED_STATUS, event.getEventType(),
                            null, Instant.now());
                }
                callbackClient.sendCallback(event.getCallbackUrl(), payload);
            } catch (InterruptedException ie) {
                // allow exit
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }

}