package com.sprih.event.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sprih.event.notifications"})
public class EventNotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventNotificationApplication.class, args);
    }
}