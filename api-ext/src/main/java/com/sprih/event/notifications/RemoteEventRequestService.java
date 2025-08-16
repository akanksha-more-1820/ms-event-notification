package com.sprih.event.notifications;

import com.sprih.event.notifications.api.request.EventRequest;
import com.sprih.event.notifications.api.response.EventResponse;

public interface RemoteEventRequestService {

    public EventResponse createEvent(EventRequest request);
}