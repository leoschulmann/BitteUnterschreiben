package com.leoschulmann.podpishiplz.controller;

import java.util.*;

public class EventController {
    private static Map<EventType, List<EventListener>> listeners = new HashMap<>();

    static {
        Arrays.stream(EventType.values()).forEach(et -> listeners.put(et, new ArrayList<>()));
    }

    public static void subscribe(EventType event, EventListener listener) {
        listeners.get(event).add(listener);
    }

    public static void unsubscribe(EventListener listener) {
        listeners.values().forEach(list -> list.remove(listener));
    }

    public static void notify(EventType event, Object object) {
        listeners.get(event).forEach(listener -> listener.eventUpdate(event, object));
    }
}
