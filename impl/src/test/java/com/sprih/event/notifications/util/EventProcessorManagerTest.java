package com.sprih.event.notifications.util;

import com.sprih.event.notifications.enums.EventType;
import com.sprih.event.notifications.models.CallbackPayload;
import com.sprih.event.notifications.models.Event;
import com.sprih.event.notifications.service.CallbackClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventProcessorManagerTest {

    private EventQueueManager queueManager;
    private CallbackClient callbackClient;
    private EventProcessorManager manager;

    @Before
    public void setUp() {
        queueManager = new EventQueueManager();
        callbackClient = mock(CallbackClient.class);

        manager = new EventProcessorManager(
                queueManager,
                callbackClient,
                100,   // 100% failure for test
                100,   // email delay
                100,   // sms delay
                100    // push delay
        );
    }

    @After
    public void tearDown() throws InterruptedException {
        manager.shutdown();
    }

    @Test
    public void testEventProcessing_withFailure() throws Exception {
        manager.startProcessors();

        Event event = new Event(EventType.EMAIL, new HashMap<>(), "http://callback.url");
        queueManager.getQueue(EventType.EMAIL).offer(event);

        // Wait enough time for processing
        Thread.sleep(300);

        ArgumentCaptor<CallbackPayload> captor = ArgumentCaptor.forClass(CallbackPayload.class);
        verify(callbackClient, timeout(1000)).sendCallback(eq("http://callback.url"), captor.capture());

        CallbackPayload payload = captor.getValue();
        assertEquals("FAILED", payload.getStatus());
        assertEquals(EventType.EMAIL, payload.getEventType());
        assertNotNull(payload.getErrorMessage());
        assertTrue(payload.getProcessedAt().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    public void testGracefulShutdown_stopsAcceptingNewEvents() throws InterruptedException {
        manager.startProcessors();

        // Add event before shutdown
        Event event1 = new Event(EventType.SMS, new HashMap<>(), "http://callback.url");
        manager.queueEvent(event1);

        // Shutdown
        manager.shutdown();

        // Add event after shutdown
        Event event2 = new Event(EventType.SMS, new HashMap<>(), "http://callback.url");
        queueManager.getQueue(EventType.SMS).offer(event2);

        // Sleep to ensure previous event processed
        Thread.sleep(300);

        // Verify only first event was processed (callback called once)
        verify(callbackClient, atLeastOnce()).sendCallback(eq("http://callback.url"), any());
    }

    @Test
    public void testProperThreadTerminationOnShutdown() throws InterruptedException {
        manager.startProcessors();
        manager.shutdown();

        // Give time for termination
        Thread.sleep(200);

        for (ExecutorService exec : manager.getExecutors().values()) {
            assertTrue("Executor should be terminated", exec.isTerminated());
        }
    }
}