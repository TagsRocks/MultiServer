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

package com.friz.network;

import com.friz.network.com.friz.network.event.EventContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Created by Kyle Fricilone on 9/8/2015.
 */
public class SessionContext<S extends NetworkServer> implements EventContext {

    private final Channel channel;
    private final S server;

    public SessionContext(Channel c, S s) {
        this.channel = c;
        this.server = s;
    }

    public ChannelFuture write(Object o) {
        return channel.writeAndFlush(o);
    }

    public Channel getChannel() {
        return channel;
    }

    public S getServer() {
        return server;
    }

}
