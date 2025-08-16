package com.sprih.event.notifications.util;

import com.sprih.event.notifications.enums.EventType;
import com.sprih.event.notifications.models.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;

public class EventQueueManagerTest {

    private EventQueueManager queueManager;

    @Before
    public void setUp() {
        queueManager = new EventQueueManager();
    }

    @Test
    public void testAllEventTypesHaveQueues() {
        for (EventType type : EventType.values()) {
            BlockingQueue<Event> queue = queueManager.getQueue(type);
            assertNotNull("Queue should not be null for type: " + type, queue);
        }
    }

    @Test
    public void testQueueFunctionality() throws InterruptedException {
        Event testEvent = new Event(EventType.EMAIL, Mockito.anyMap(), "http://callback.url");

        BlockingQueue<Event> emailQueue = queueManager.getQueue(EventType.EMAIL);
        emailQueue.put(testEvent);  // Add to queue
        Event retrievedEvent = emailQueue.take();  // Remove from queue

        assertEquals(testEvent, retrievedEvent);
    }

    @Test
    public void testQueueInstancesAreDifferent() {
        assertNotSame(
                queueManager.getQueue(EventType.EMAIL),
                queueManager.getQueue(EventType.SMS)
        );
    }
}