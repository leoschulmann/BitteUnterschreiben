package com.leoschulmann.podpishiplz.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class EventController {
    private static final Map<EventType, List<EventListener>> listeners = new HashMap<>();

    static {
        Arrays.stream(EventType.values()).forEach(et -> listeners.put(et, new ArrayList<>()));
    }

    public static void subscribe(EventType event, EventListener listener) {
        log.debug("Subscribing {} to {}.", listener.getClass().getSimpleName(), event);
        listeners.get(event).add(listener);
    }

    public static void unsubscribe(EventListener listener) {
        log.debug("Unsubscribing listener {}.", listener.getClass().getSimpleName());
        listeners.values().forEach(list -> list.remove(listener));
    }

    public static void notify(EventType event, Object object) {
        log.debug("Notifying about event {}, payload {}.", event, object);
        listeners.get(event).forEach(listener -> listener.eventUpdate(event, object));
    }
}
