package com.leoschulmann.podpishiplz.controller;

import org.slf4j.LoggerFactory;

import java.util.*;

public class EventController {
    private static Map<EventType, List<EventListener>> listeners = new HashMap<>();

    static {
        Arrays.stream(EventType.values()).forEach(et -> listeners.put(et, new ArrayList<>()));
    }

    public static void subscribe(EventType event, EventListener listener) {
        LoggerFactory.getLogger(EventController.class).debug("Subscribing {} to {}.",
                listener.getClass().getSimpleName(), event);
        listeners.get(event).add(listener);
    }

    public static void unsubscribe(EventListener listener) {
        LoggerFactory.getLogger(EventController.class).debug("Unsubscribing listener {}.",
                listener.getClass().getSimpleName());
        listeners.values().forEach(list -> list.remove(listener));
    }

    public static void notify(EventType event, Object object) {
        LoggerFactory.getLogger(EventController.class).debug("Notifying about event {}, payload {}.", event, object);
        listeners.get(event).forEach(listener -> listener.eventUpdate(event, object));
    }
}
