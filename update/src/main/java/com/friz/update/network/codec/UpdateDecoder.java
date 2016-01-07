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

import com.friz.update.network.events.FileRequestEvent;
import com.friz.update.network.events.XorRequestEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Kyle Fricilone on 9/20/2015.
 */
public final class UpdateDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (!buffer.isReadable(6))
			return;

		int opcode = buffer.readUnsignedByte();
		if (opcode == 0 || opcode == 1) {
			int type = buffer.readUnsignedByte();
			int file = buffer.readInt();
			out.add(new FileRequestEvent(opcode == 1, type, file));
		} else if (opcode == 4) {
			int key = buffer.readUnsignedByte();
			buffer.readerIndex(buffer.readerIndex() + 4);
			out.add(new XorRequestEvent(key));
		} else {
			buffer.readerIndex(buffer.readerIndex() + 5);
		}
	}
}
