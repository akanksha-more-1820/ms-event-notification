package com.sprih.event.notifications.service;

import com.sprih.event.notifications.FailureDecider;

import java.util.concurrent.ThreadLocalRandom;

public class RandomFailureDecider implements FailureDecider {
    private final int failureRatePercent;
    public RandomFailureDecider(int failureRatePercent) {
        this.failureRatePercent = failureRatePercent;
    }
    @Override
    public boolean shouldFail() {
        return ThreadLocalRandom.current().nextInt(100) < failureRatePercent;
    }
}