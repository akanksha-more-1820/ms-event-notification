package com.sprih.event.notifications.service;

import com.sprih.event.notifications.FailureDecider;
import com.sprih.event.notifications.enums.EventType;
import com.sprih.event.notifications.models.CallbackPayload;
import com.sprih.event.notifications.models.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventProcessorTest {

    private CallbackClient callbackClient;
    private FailureDecider failureDecider;
    private BlockingQueue<Event> queue;
    private Thread processorThread;
    @InjectMocks
    private EventProcessor processor;

    @Before
    public void setUp() {
        callbackClient = mock(CallbackClient.class);
        failureDecider = mock(FailureDecider.class);
        queue = new ArrayBlockingQueue<>(10);
    }

    @Test
    public void testRun_shouldProcessEventSuccessfully() throws InterruptedException {
        when(failureDecider.shouldFail()).thenReturn(false);
        Event event = new Event(EventType.EMAIL, new HashMap<>(), "http://callback.com");
        queue.offer(event);

        processor = new EventProcessor(queue, 10, callbackClient, failureDecider);
        Thread thread = new Thread(processor);
        thread.start();

        Thread.sleep(50);
        processor.stop();
        thread.join(100);

        ArgumentCaptor<CallbackPayload> captor = ArgumentCaptor.forClass(CallbackPayload.class);
        verify(callbackClient, times(1)).sendCallback(eq("http://callback.com"), captor.capture());

        CallbackPayload payload = captor.getValue();
        assertEquals(event.getEventId(), payload.getEventId());
        assertEquals("COMPLETED", payload.getStatus());
    }

    @Test
    public void testRun_shouldFailEvent() throws InterruptedException {
        when(failureDecider.shouldFail()).thenReturn(true);
        Event event = new Event(EventType.SMS, new HashMap<>(), "http://callback.com");
        queue.offer(event);

        processor = new EventProcessor(queue, 10, callbackClient, failureDecider);
        Thread thread = new Thread(processor);
        thread.start();

        Thread.sleep(50);
        processor.stop();
        thread.join(100);

        ArgumentCaptor<CallbackPayload> captor = ArgumentCaptor.forClass(CallbackPayload.class);
        verify(callbackClient, times(1)).sendCallback(eq("http://callback.com"), captor.capture());

        CallbackPayload payload = captor.getValue();
        assertEquals("FAILED", payload.getStatus());
        assertNotNull(payload.getErrorMessage());
    }

    @Test
    public void testProcessorHandlesInterruptGracefully() throws InterruptedException {
        processorThread = new Thread(processor);
        processorThread.start();

        Thread.sleep(50);

        processorThread.interrupt();

        processorThread.join(500);

        verify(callbackClient, never()).sendCallback(anyString(), any());
    }
}