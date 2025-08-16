package com.sprih.event.notifications.controller;

import com.sprih.event.notifications.EventService;
import com.sprih.event.notifications.RemoteEventRequestService;
import com.sprih.event.notifications.api.request.EventRequest;
import com.sprih.event.notifications.api.response.EventResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.sprih.event.notifications.Constants.EVENTS_BASE_URL;

@RestController
@RequestMapping(EVENTS_BASE_URL)
public class EventController implements RemoteEventRequestService {

    @Autowired
    private EventService eventService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@Valid @RequestBody EventRequest request) {
        return eventService.createEvent(request);
    }

}