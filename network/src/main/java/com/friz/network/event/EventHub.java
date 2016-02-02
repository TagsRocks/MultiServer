/*
 * MultiServer - Multiple Server Communication Application
 * Copyright (C) 2015 Kyle Fricilone
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.friz.network.event;

import com.friz.network.event.impl.RecycleEvent;
import com.friz.network.event.impl.RecycleEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kyle Fricilone on 9/8/2015.
 */
public class EventHub {

    private final Map<Class<?>, List<EventListener<?, ?>>> listenerMap = new HashMap<>();

    public EventHub() {
        listen(RecycleEvent.class, new RecycleEventListener());
    }

    public void listen(Class<? extends Event> c, EventListener<?, ?> l) {
        List<EventListener<?, ?>> listeners = listenerMap.get(c);
        if (listeners == null) {
            listeners = new LinkedList<>();
            listenerMap.put(c, listeners);
        }
        listeners.add(l);
    }

    public void unlisten(Class<? extends Event> c) {
        listenerMap.remove(c);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void onLink(EventLink l) {
        List<EventListener<?, ?>> listeners = listenerMap.get(l.getEvent().getClass());
        if (listeners != null) {
            listeners.forEach((EventListener listener) -> {
                listener.onEvent(l.getEvent(), l.getContext());
            });
        }
    }

    public int size() {
        return listenerMap.size();
    }

    public int size(Class<?> c) {
        return listenerMap.get(c).size();
    }

}
