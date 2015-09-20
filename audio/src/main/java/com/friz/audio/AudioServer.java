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
package com.friz.audio;

import com.friz.audio.network.AudioChannelHandler;
import com.friz.audio.network.events.AudioRequestEvent;
import com.friz.audio.network.listeners.AudioRequestEventListener;
import com.friz.cache.Cache;
import com.friz.cache.Container;
import com.friz.cache.ReferenceTable;
import com.friz.network.Constants;
import com.friz.network.NetworkServer;
import com.friz.network.SessionContext;
import com.friz.network.com.friz.network.event.EventHub;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by Kyle Fricilone on 9/17/2015.
 */
public class AudioServer extends NetworkServer {

    private final Cache cache;
    private final EventHub hub = new EventHub();
    private final AttributeKey<SessionContext> attr = AttributeKey.valueOf("audio-attribute-key");

    private ByteBuffer checksum;
    private ReferenceTable reference;

    public AudioServer(Cache c) {
        this.cache = c;
        try {
            this.checksum = new Container(Container.COMPRESSION_NONE, c.createChecksumTable().encode(true, Constants.ONDEMAND_MODULUS, Constants.ONDEMAND_EXPONENT)).encode();
            this.reference = ReferenceTable.decode(Container.decode(c.getStore().read(255, 40)).getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {
        group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        bootstrap = new ServerBootstrap();

        AudioServer s = this;

        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(HttpServerCodec.class.getName(), new HttpServerCodec());
                        p.addLast(HttpObjectAggregator.class.getName(), new HttpObjectAggregator(65536));
                        p.addLast(ChunkedWriteHandler.class.getName(), new ChunkedWriteHandler());
                        p.addLast(AudioChannelHandler.class.getName(), new AudioChannelHandler(s));
                    }

                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true);

        hub.listen(AudioRequestEvent.class, new AudioRequestEventListener());
    }

    @Override
    public void bind() {
        try {
            future = bootstrap.bind(new InetSocketAddress("0.0.0.0", 80)).sync();
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
        }
    }

    public final Cache getCache() {
        return cache;
    }

    public final ByteBuffer getChecksum() {
        return checksum;
    }

    public final ReferenceTable getReference() {
        return reference;
    }

    public EventHub getHub() {
        return hub;
    }

    public AttributeKey<SessionContext> getAttr() {
        return attr;
    }

}
