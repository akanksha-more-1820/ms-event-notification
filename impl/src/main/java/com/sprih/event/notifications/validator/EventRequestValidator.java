package com.sprih.event.notifications.validator;

import com.sprih.event.notifications.api.request.EventRequest;
import org.springframework.stereotype.Component;

import static com.sprih.event.notifications.Constants.*;

@Component
public class EventRequestValidator {

    public void validate(EventRequest request) {
        if (request.getEventType() == null || request.getPayload() == null) {
            throw new IllegalArgumentException(MISSING_EVENT_TYPE_OR_PAYLOAD);
        }

        switch (request.getEventType()) {
            case EMAIL -> {
                if (!request.getPayload().containsKey(RECIPIENT) || !request.getPayload().containsKey(MESSAGE)) {
                    throw new IllegalArgumentException(EMAIL_PAYLOAD_INVALID);
                }
            }
            case SMS -> {
                if (!request.getPayload().containsKey(PHONE_NUMBER) || !request.getPayload().containsKey(MESSAGE)) {
                    throw new IllegalArgumentException(SMS_PAYLOAD_INVALID);
                }
            }
            case PUSH -> {
                if (!request.getPayload().containsKey(DEVICE_ID) || !request.getPayload().containsKey(MESSAGE)) {
                    throw new IllegalArgumentException(PUSH_PAYLOAD_INVALID);
                }
            }
        }
    }

}