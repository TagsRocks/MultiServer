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
import com.friz.network.SessionContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;

import java.nio.ByteBuffer;

/**
 * Created by Kyle Fricilone on 9/18/2015.
 */
public class AudioSessionContext extends SessionContext<AudioServer> {

    public AudioSessionContext(Channel c, AudioServer audioServer) {
        super(c, audioServer);
    }

    public void writeResponse(HttpVersion version, ByteBuf container) {
        HttpResponse response = new DefaultHttpResponse(version, HttpResponseStatus.OK);
        HttpHeaders.setContentLength(response, container.readableBytes());

        channel.write(response);
        channel.write(container);
        channel.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

}
