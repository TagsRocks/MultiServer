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

package com.friz.audio.network.listeners;

import com.friz.audio.network.AudioSessionContext;
import com.friz.audio.network.events.AudioRequestEvent;
import com.friz.audio.network.events.FileRequestEvent;
import com.friz.network.event.EventListener;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.Deque;

/**
 * Created by Kyle Fricilone on 9/18/2015.
 */
public class AudioRequestEventListener implements EventListener<AudioRequestEvent, AudioSessionContext> {

    @Override
    public void onEvent(AudioRequestEvent event, AudioSessionContext context) {
        int type = -1, file = -1, crc = -1, version = -1;

        QueryStringDecoder query = new QueryStringDecoder(event.getRequest().getUri());
        for (String key : query.parameters().keySet()) {
            if (key.equals("a"))
                type = Integer.valueOf(query.parameters().get(key).get(0));
            else if (key.equals("g"))
                file = Integer.valueOf(query.parameters().get(key).get(0));
            else if (key.equals("c"))
                crc = Integer.valueOf(query.parameters().get(key).get(0));
            else if (key.equals("v"))
                version = Integer.valueOf(query.parameters().get(key).get(0));
        }

        Deque<FileRequestEvent> queue = context.getFileQueue();
        synchronized (queue) {
            queue.add(new FileRequestEvent(type, file, crc, version, event.getRequest().getProtocolVersion()));
            if (context.isIdle()) {
                context.addContextToService();
                context.setIdle(false);
            }
        }
    }
}
