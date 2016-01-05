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

package com.friz.update.network;

import com.friz.network.SessionContext;
import com.friz.update.UpdateServer;
import com.friz.update.UpdateService;
import com.friz.update.network.codec.*;
import com.friz.update.network.events.FileRequestEvent;
import com.friz.update.network.events.FileResponseEvent;
import com.friz.update.network.events.UpdateResponseEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Kyle Fricilone on 9/20/2015.
 */
public class UpdateSessionContext extends SessionContext<UpdateServer> {

    /**
     * The {@link java.util.Deque} of {@link com.friz.update.network.events.FileRequestEvent}s.
     */
    private final Deque<FileRequestEvent> fileQueue = new ArrayDeque<>();

    /**
     * THe idle flag.
     */
    private boolean idle = true;

    /**
     * THe handshake complete flag.
     */
    private boolean handshakeComplete = false;

    /**
     * The {@link com.friz.update.UpdateService}.
     */
    private final UpdateService service;

    public UpdateSessionContext(Channel c, UpdateServer updateServer) {
        super(c, updateServer);
        this.service = updateServer.getService();
    }

    public void writeSuccess(int status) {
        channel.writeAndFlush(new UpdateResponseEvent(status)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ChannelPipeline p = future.channel().pipeline();
                    p.remove(UpdateInitDecoder.class);
                    p.remove(UpdateInitEncoder.class);

                    p.addFirst(XorEncoder.class.getName(), new XorEncoder());
                    p.addFirst(UpdateEncoder.class.getName(), new UpdateEncoder());
                    p.addFirst(UpdateDecoder.class.getName(), new UpdateDecoder());

                    handshakeComplete = true;
                }
            }
        });
    }

    public void writeFailure(int status) {
        channel.writeAndFlush(new UpdateResponseEvent(status)).addListener(ChannelFutureListener.CLOSE);
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
                service.addUpdateContext(this);
                idle = false;
            }
        }
        if (request != null) {
            int type = request.getType();
            int file = request.getFile();
            ByteBuf buf;
            try {
                if (type == 255 && file == 255) {
                    buf = Unpooled.wrappedBuffer(server.getCache().getChecksum());
                } else {
                    buf = Unpooled.wrappedBuffer(server.getCache().getStore().read(type, file));
                    if (type != 255)
                        buf = buf.slice(0, buf.readableBytes() - 2);
                }
                channel.writeAndFlush(new FileResponseEvent(request.isPriority(), type, file, buf));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
     * Returns the {@code handshakeComplete} flag.
     * @return The {@code handshakeComplete}
     */
    public boolean isHandshakeComplete() {
        return handshakeComplete;
    }

    /**
     * Sets the {@code idle} flag.
     * @param idle the {@code idle} flag to set.
     */
    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    /**
     * Sets the {@code handshakeComplete} flag.
     * @param handshakeComplete the handshakeComplete to set
     */
    public void setHandshakeComplete(boolean handshakeComplete) {
        this.handshakeComplete = handshakeComplete;
    }

    /**
     * Adds this {@link com.friz.update.network.UpdateSessionContext} to the {@link com.friz.update.UpdateService}.
     */
    public void addContextToService() {
        service.addUpdateContext(this);
    }

}
