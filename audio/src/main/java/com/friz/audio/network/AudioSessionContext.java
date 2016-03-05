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

package com.friz.audio.network;

import com.friz.audio.AudioServer;
import com.friz.audio.AudioService;
import com.friz.audio.network.events.FileRequestEvent;
import com.friz.network.SessionContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Kyle Fricilone on 9/18/2015.
 */
public class AudioSessionContext extends SessionContext<AudioServer> {

    /**
     * THe {@link java.util.Deque} of {@link com.friz.audio.network.events.FileRequestEvent}s.
     */
    private final Deque<FileRequestEvent> fileQueue = new ArrayDeque<>();

    /**
     * THe idle flag.
     */
    private boolean idle = true;

    /**
     * The {@link com.friz.audio.AudioService}.
     */
    private final AudioService service;

    public AudioSessionContext(Channel c, AudioServer audioServer) {
        super(c, audioServer);
        this.service = audioServer.getService();
    }

    public void writeResponse(HttpVersion version, ByteBuf container) {
        HttpResponse response = new DefaultHttpResponse(version, HttpResponseStatus.OK);
        HttpHeaders.setContentLength(response, container.readableBytes());

        channel.write(response);
        channel.write(container);
        channel.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    /**
     * Processes the file requests.
     * @throws IOException The exception thrown if an i/o error occurs.
     */
    public void processFileQueue() {
        FileRequestEvent request;
        synchronized (fileQueue) {
            request = fileQueue.pop();
            if (fileQueue.isEmpty()) {
                idle = true;
            } else {
                service.addAudioContext(this);
                idle = false;
            }
        }
        if (request != null) {
            int type = request.getType();
            int file = request.getFile();
            int crc = request.getCrc();
            int version = request.getVersion();
            HttpVersion http = request.getHttp();

            ByteBuf buf = Unpooled.buffer();
            if (type == 255 && file == 255) {
                buf = Unpooled.wrappedBuffer(server.getCache().getChecksum());
            } else {
                if (server.getCache().getReferenceTable(type).getEntry(file).getCrc() != crc
                        || server.getCache().getReferenceTable(type).getEntry(file).getVersion() != version) {
                    writeResponse(http, buf);
                    return;
                }

                try {
                    buf = Unpooled.wrappedBuffer(server.getCache().getStore().read(type, file));
                    if (type != 255)
                        buf = buf.slice(0, buf.readableBytes() - 2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            writeResponse(http, buf);
        }
    }

    /**
     * Gets the {@link Deque} of {@link FileRequestEvent}s.
     * @return The {@code fileQueue}.
     */
    public Deque<FileRequestEvent> getFileQueue() {
        return fileQueue;
    }

    /**
     * Returns the {@code idle} flag.
     * @return The {@code idle}.
     */
    public boolean isIdle() {
        return idle;
    }

    /**
     * Sets the {@code idle} flag.
     * @param idle the {@code idle} flag to set.
     */
    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    /**
     * Adds this {@link com.friz.update.network.UpdateSessionContext} to the {@link com.friz.update.UpdateService}.
     */
    public void addContextToService() {
        service.addAudioContext(this);
    }

}
