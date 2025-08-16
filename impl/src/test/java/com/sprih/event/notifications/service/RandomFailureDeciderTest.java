package com.sprih.event.notifications.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomFailureDeciderTest {

    @Test
    public void testShouldNeverFailWhenZeroPercent() {
        RandomFailureDecider decider = new RandomFailureDecider(0);
        for (int i = 0; i < 1000; i++) {
            assertFalse("Expected to never fail when failureRatePercent is 0", decider.shouldFail());
        }
    }

    @Test
    public void testShouldAlwaysFailWhenHundredPercent() {
        RandomFailureDecider decider = new RandomFailureDecider(100);
        for (int i = 0; i < 1000; i++) {
            assertTrue("Expected to always fail when failureRatePercent is 100", decider.shouldFail());
        }
    }

    @Test
    public void testShouldFailRoughlyExpectedRate() {
        int failureRate = 30;
        RandomFailureDecider decider = new RandomFailureDecider(failureRate);
        int failCount = 0;
        int total = 10_000;

        for (int i = 0; i < total; i++) {
            if (decider.shouldFail()) {
                failCount++;
            }
        }

        double actualRate = (failCount * 100.0) / total;
        assertTrue("Failure rate should be around " + failureRate + "%, got: " + actualRate,
                actualRate > (failureRate - 5) && actualRate < (failureRate + 5));
    }
}