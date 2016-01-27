/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.jdocker.events;

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
