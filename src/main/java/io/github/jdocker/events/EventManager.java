package io.github.jdocker.events;

import io.github.jdocker.events.JDockerEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by atsticks on 21.01.16.
 */
public final class EventManager {

    private static Map<Class<? extends JDockerEvent>, List<Consumer<JDockerEvent>>> listeners =
            new ConcurrentHashMap<>();

    private EventManager(){}

    public static void publishEvent(JDockerEvent action){
        List<Consumer<JDockerEvent>> consumers = listeners.get(JDockerEvent.class);
        if(consumers!=null) {
            for (Consumer<JDockerEvent> l : consumers) {
                l.accept(action);
            }
        }
        Class target = action.getClass();
        while(target!=JDockerEvent.class) {
            consumers = listeners.get(target);
            if (consumers != null) {
                for (Consumer<JDockerEvent> l : consumers) {
                    l.accept(action);
                }
            }
            target = target.getSuperclass();
        }
    }

    public static void addActionListener(Consumer<JDockerEvent> listener){
        addActionListener(listener, JDockerEvent.class);
    }

    public static void removeActionListener(Consumer<JDockerEvent> listener){
        removeActionListener(listener, JDockerEvent.class);
    }

    public static void addActionListener(Consumer<JDockerEvent> listener,
                                         Class<? extends JDockerEvent> eventType){
        List<Consumer<JDockerEvent>> consumers = listeners.get(JDockerEvent.class);
        if(consumers==null){
            consumers = new ArrayList<>();
            listeners.put(eventType, consumers);
        }
        consumers.add(listener);
    }

    public static void removeActionListener(Consumer<JDockerEvent> listener,
                                            Class<? extends JDockerEvent> eventType){
        List<Consumer<JDockerEvent>> consumers = listeners.get(JDockerEvent.class);
        if(consumers!=null){
            consumers.remove(listener);
        }
    }


}
