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

package com.friz.network.utility;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * @author Sean
 */
public final class BufferUtils {

	/**
	 * Puts a 5 byte integer into the buffer.
	 * @param buf The channel buffer
	 * @param value The value to be added.
	 */
	public static void put5ByteInteger(ByteBuf buf, long value) {
		buf.writeByte((int) (value >> 32));
		buf.writeInt((int) (value & 0xffffffff));
	}

    /**
     * Puts a 6 byte integer into the buffer.
     * @param buf The channel buffer
     * @param value The value to be added.
     */
    public static void put6ByteInteger(ByteBuf buf, long value) {
        buf.writeShort((int) (value >> 32));
        buf.writeInt((int) (value & 0xffffffff));
    }
	
	/**
	 * Writes a string
	 * @param buffer The ChannelBuffer
	 * @param string The string being wrote.
	 */
	public static void putJagString(ByteBuf buffer, String string) {
		buffer.writeByte(0);
		buffer.writeBytes(string.getBytes());
		buffer.writeByte(0);
	}
	
	/**
	 * Puts a string into a buffer.
	 * @param buf The buffer.
	 * @param string The string.
	 */
	public static void putString(ByteBuf buf, String string) {
		for(char c : string.toCharArray()) {
			buf.writeByte(c);
		}
		buf.writeByte(0);
	}
	
	/**
	 * Writes a 'tri-byte' to the specified buffer.
	 * @param buf The buffer.
	 * @param value The value.
	 */
	public static void putTriByte(ByteBuf buf, int value) {
		buf.writeByte(value >> 16);
		buf.writeByte(value >> 8);
		buf.writeByte(value);
	}
	
	/**
	 * Reads a string from a bytebuffer.
	 * @param buf The bytebuffer.
	 * @return The decoded string.
	 */
	public static String readString(ByteBuffer buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while((b = buf.get()) != 0) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}
	
	/**
	 * Reads a RuneScape string from a buffer.
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static String readString(ByteBuf buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while(buf.isReadable() && (b = buf.readByte()) != 0) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

    /**
     * Adds a int into a buffer.
     * @param val The value to add.
     * @param index The index to add the value.
     * @param buffer The buffer to add the value to.
     */
    public static void putInt(int val, int index, byte[] buffer) {
        buffer[index++] = (byte) (val >> 24);
        buffer[index++] = (byte) (val >> 16);
        buffer[index++] = (byte) (val >> 8);
        buffer[index++] = (byte) val;
    }

    /**
     * Gets an int from a buffer.
     * @param index The index of the buffer to read the value from.
     * @param buffer The buffer containing the value to get.
     * @return The decoded value.
     */
    public static int getInt(int index, byte[] buffer) {
        return ((buffer[index++] & 0xff) << 24) | ((buffer[index++] & 0xff) << 16) | ((buffer[index++] & 0xff) << 8) | (buffer[index++] & 0xff);
    }

    /**
     * Encodes a {@link String} with CESU8.
     * @param string The {@link String} to encode.
     * @return The encoded {@link String} in a {@link ByteBuf}.
     */
    public static ByteBuf encodeCESU8(String string) {
        int length = string.length();

        // Calculate the amount of bytes for the buffer.
        int size = 0;
        for (int index = 0; index < length; index++) {
            int character = string.charAt(index);
            if (character >= 2048)
                size += 3;
            else if (character >= 128)
                size += 2;
            else
                size++;
        }

        // Allocate a new buffer for appending data.
        ByteBuf buffer = Unpooled.buffer(size);

        for (int index = 0; index < length; index++) {
            // Get the character at the current index.
            int character = string.charAt(index);

            // A character that is represented more than 1 bytes.
            if (character >= 128) {

                // A character that is represented by 3 bytes.
                if (character >= 2048) {
                    buffer.writeByte((character >> 0xC) | 0xE0);
                    buffer.writeByte(((character >> 6) & 0x3F) | 0x80);
                    buffer.writeByte((character & 0x3F) | 0x80);

                    // A character that is represented by 2 bytes.
                } else {
                    buffer.writeByte((character >> 6) | 0x3015);
                    buffer.writeByte((character & 0x3F) | 0x80);
                }
            } else {
                // A character in which is represented by a single byte.
                buffer.writeByte(character);
            }
        }
        return buffer;
    }
}
