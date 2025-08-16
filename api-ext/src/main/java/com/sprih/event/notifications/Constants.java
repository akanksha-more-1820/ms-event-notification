package com.sprih.event.notifications;

public class Constants {

    public static final String CALLBACK_FAILURE_MSG = "Callback failed for";
    public static final String COLON = ": ";
    public static final String FAILED_STATUS = "FAILED";
    public static final String COMPLETED_STATUS = "COMPLETED";
    public static final String SIMULATED_PROCESS_FAILURE_MESSAGE = "Simulated processing failure";
    public static final String SYSTEM_SHUTDOWN_MESSAGE = "System is shutting down. No new events accepted.";
    public static final String EVENT_ACCEPTED_MESSAGE = "Event accepted for processing.";
    public static final String PROCESSOR = "-processor";
    public static final String MISSING_EVENT_TYPE_OR_PAYLOAD = "eventType and payload are required";
    public static final String RECIPIENT = "recipient";
    public static final String MESSAGE = "message";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String DEVICE_ID = "deviceId";
    public static final String EMAIL_PAYLOAD_INVALID =
            "EMAIL payload must contain 'recipient' and 'message'";
    public static final String SMS_PAYLOAD_INVALID =
            "SMS payload must contain 'phoneNumber' and 'message'";
    public static final String PUSH_PAYLOAD_INVALID =
            "PUSH payload must contain 'deviceId' and 'message'";
    public static final String EVENTS_BASE_URL = "/api/events";
}