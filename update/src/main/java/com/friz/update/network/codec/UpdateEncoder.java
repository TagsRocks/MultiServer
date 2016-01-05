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

import com.friz.cache.Sector;
import com.friz.update.network.events.FileResponseEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Kyle Fricilone on 9/20/2015.
 */
public final class UpdateEncoder extends MessageToByteEncoder<FileResponseEvent> {

	
	/**
	 * The remaining bytes after the header.
	 */
	private static final int BYTES_REMAINING_AFTER_HEADER = Sector.DATA_SIZE - 6;
	
	/**
	 * The remaining bytes after the block.
	 */
	private static final int BYTES_REMAINING_AFTER_BLOCK = Sector.DATA_SIZE - 5;

    @Override
	protected void encode(ChannelHandlerContext ctx, FileResponseEvent msg, ByteBuf buffer) throws Exception {
		ByteBuf container = msg.getContainer();
		int compression = container.readUnsignedByte();
		int type = msg.getType();
		int file = msg.getFile();

		if (!msg.isPriority())
			file |= 0x80000000;
			
		buffer.writeByte(type);
		buffer.writeInt(file);
		buffer.writeByte(compression);
			
		int bytes = container.readableBytes();
		if (bytes > BYTES_REMAINING_AFTER_HEADER)
			bytes = BYTES_REMAINING_AFTER_HEADER;

		buffer.writeBytes(container.readBytes(bytes));

		for (;;) {
			bytes = container.readableBytes();
			if (bytes == 0)
				break;
			else if (bytes > BYTES_REMAINING_AFTER_BLOCK)
				bytes = BYTES_REMAINING_AFTER_BLOCK;

			buffer.writeByte(type);
			buffer.writeInt(file);
			buffer.writeBytes(container.readBytes(bytes));
		}
	}
	
}

