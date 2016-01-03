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
package com.friz.update;

import com.friz.cache.Cache;
import com.friz.network.NetworkServer;
import com.friz.network.SessionContext;
import com.friz.network.event.EventHub;
import com.friz.update.network.UpdateChannelHandler;
import com.friz.update.network.codec.*;
import com.friz.update.network.events.FileRequestEvent;
import com.friz.update.network.events.XorRequestEvent;
import com.friz.update.network.events.UpdateRequestEvent;
import com.friz.update.network.listeners.XorRequestEventListener;
import com.friz.update.network.listeners.FileRequestEventListener;
import com.friz.update.network.listeners.UpdateRequestEventListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * Created by Kyle Fricilone on 9/7/2015.
 */
public class UpdateServer extends NetworkServer {

    private final Cache cache;
    private final EventHub hub = new EventHub();
    private final UpdateService service = new UpdateService();
    private final AttributeKey<SessionContext> attr = AttributeKey.valueOf("update-attribute-key");

    public UpdateServer(Cache c) {
        this.cache = c;
    }

    @Override
    public void initialize() {
        group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        bootstrap = new ServerBootstrap();

        UpdateServer s = this;

        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(UpdateInitEncoder.class.getName(), new UpdateInitEncoder());
                        p.addLast(UpdateInitDecoder.class.getName(), new UpdateInitDecoder());
                        p.addLast(IdleStateHandler.class.getName(), new IdleStateHandler(15, 0, 0));
                        p.addLast(UpdateChannelHandler.class.getName(), new UpdateChannelHandler(s));
                    }

                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true);

        hub.listen(UpdateRequestEvent.class, new UpdateRequestEventListener());
        hub.listen(XorRequestEvent.class, new XorRequestEventListener());
        hub.listen(FileRequestEvent.class, new FileRequestEventListener());

        service.startAsync();
    }

    @Override
    public void bind() {
        try {
            future = bootstrap.bind(new InetSocketAddress("0.0.0.0", 43594)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            service.stopAsync();
        }
    }

    public final Cache getCache() {
        return cache;
    }

    public EventHub getHub() {
        return hub;
    }

    public AttributeKey<SessionContext> getAttr() {
        return attr;
    }

    public UpdateService getService() {
        return service;
    }
}
