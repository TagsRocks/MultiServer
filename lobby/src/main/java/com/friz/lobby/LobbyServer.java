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
package com.friz.lobby;

import com.friz.cache.Cache;
import com.friz.lobby.network.LobbyChannelHandler;
import com.friz.lobby.network.codec.LoginInitDecoder;
import com.friz.lobby.network.codec.LoginInitEncoder;
import com.friz.network.NetworkServer;
import com.friz.network.SessionContext;
import com.friz.network.event.EventHub;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * Created by Kyle Fricilone on 9/22/2015.
 */
public class LobbyServer extends NetworkServer {

    private final Cache cache;

    private final EventHub hub = new EventHub();
    private final AttributeKey<SessionContext> attr = AttributeKey.valueOf("lobby-attribute-key");

    public LobbyServer(Cache c) {
        this.cache = c;
    }

    @Override
    public void initialize() {
        group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        bootstrap = new ServerBootstrap();

        LobbyServer s = this;

        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                //.handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(LoginInitEncoder.class.getName(), new LoginInitEncoder());
                        p.addLast(LoginInitDecoder.class.getName(), new LoginInitDecoder());
                        p.addLast(LobbyChannelHandler.class.getName(), new LobbyChannelHandler(s));
                    }

                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true);
    }

    @Override
    public void bind() {
        try {
            future = bootstrap.bind(new InetSocketAddress("0.0.0.0", 40000)).sync();
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

    public EventHub getHub() {
        return hub;
    }

    public AttributeKey<SessionContext> getAttr() {
        return attr;
    }
}
