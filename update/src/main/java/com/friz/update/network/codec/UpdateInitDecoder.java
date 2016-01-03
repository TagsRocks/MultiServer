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

package com.friz.update.network.codec;

import com.friz.network.utility.BufferUtils;
import com.friz.update.network.events.UpdateRequestEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Kyle Fricilone on 9/24/2015.
 */
public class UpdateInitDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int type = buf.readUnsignedByte();
        int size = buf.readUnsignedByte();

        if (buf.readableBytes() < size)
            return;

        int version = buf.readInt();
        int subVersion = buf.readInt();
        String key = BufferUtils.getString(buf);
        int langId = buf.readUnsignedByte();
        out.add(new UpdateRequestEvent(type, version, subVersion, key, langId));
    }

}
