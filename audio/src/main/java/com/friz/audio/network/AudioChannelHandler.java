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
import com.friz.audio.network.events.AudioRequestEvent;
import com.friz.network.event.EventLink;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created by Kyle Fricilone on 9/18/2015.
 */
public class AudioChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final AudioServer server;

    public AudioChannelHandler(AudioServer s) {
        this.server = s;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("[AudioServer] Channel Connected from: " + ctx.channel().remoteAddress().toString());
        ctx.channel().attr(server.getAttr()).set(new AudioSessionContext(ctx.channel(), server));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        server.getHub().onLink(new EventLink(new AudioRequestEvent(msg), ctx.channel().attr(server.getAttr()).get()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
