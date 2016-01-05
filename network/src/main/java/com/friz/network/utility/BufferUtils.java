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
		buf.writeInt((int) (value));
	}

    /**
     * Puts a 6 byte integer into the buffer.
     * @param buf The channel buffer
     * @param value The value to be added.
     */
    public static void put6ByteInteger(ByteBuf buf, long value) {
        buf.writeShort((int) (value >> 32));
        buf.writeInt((int) (value));
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
	public static void putTriByte(ByteBuffer buf, int value) {
		buf.put((byte) (value >> 16));
		buf.put((byte) (value >> 8));
		buf.put((byte) value);
	}
	
	/**
	 * Reads a string from a bytebuffer.
	 * @param buf The bytebuffer.
	 * @return The decoded string.
	 */
	public static String getString(ByteBuffer buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while(buf.hasRemaining() && (b = buf.get()) != 0) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

    /**
     * Reads a string from a bytebuf.
     * @param buf The bytebuf.
     * @return The decoded string.
     */
	public static String getString(ByteBuf buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while(buf.isReadable() && (b = buf.readByte()) != 0) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

    /**
     * Reads a string from a bytebuf.
     * @param buf The bytebuf.
     * @return The decoded string.
     */
    public static String getBase37(ByteBuffer buf) {
        long value = buf.getLong();

        char[] chars = new char[12];
        int pos = 0;
        while (value != 0) {
            int remainder = (int) (value % 37);
            value /= 37;

            char c;
            if (remainder >= 1 && remainder <= 26)
                c = (char) ('a' + remainder - 1);
            else if (remainder >= 27 && remainder <= 36)
                c = (char) ('0' + remainder - 27);
            else
                c = '_';

            chars[chars.length - pos++ - 1] = c;
        }
        return new String(chars, chars.length - pos, pos);
    }

    /**
     * Reads a string from a bytebuf.
     * @param buf The bytebuf.
     * @return The decoded string.
     */
    public static String getBase37(ByteBuf buf) {
        long value = buf.readLong();

        char[] chars = new char[12];
        int pos = 0;
        while (value != 0) {
            int remainder = (int) (value % 37);
            value /= 37;

            char c;
            if (remainder >= 1 && remainder <= 26)
                c = (char) ('a' + remainder - 1);
            else if (remainder >= 27 && remainder <= 36)
                c = (char) ('0' + remainder - 27);
            else
                c = '_';

            chars[chars.length - pos++ - 1] = c;
        }
        return new String(chars, chars.length - pos, pos);
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
     * @param buf  The {@link ByteBuf} to put the encoded string.
     * @param string The {@link String} to encode.
     */
    public static void putCESU8(ByteBuf buf, String string) {
        int length = string.length();

        // Calculate the amount of bytes for the buffer.
        int bytes = 0;
        for (int index = 0; index < length; index++) {
            int character = string.charAt(index);
            if (character >= 2048)
                bytes += 3;
            else if (character >= 128)
                bytes += 2;
            else
                bytes++;
        }

        // Allocate a new buffer for appending data.
        ByteBuffer buffer = ByteBuffer.allocate(bytes);

        for (int index = 0; index < length; index++) {
            // Get the character at the current index.
            int character = string.charAt(index);

            // A character that is represented more than 1 bytes.
            if (character >= 128) {

                // A character that is represented by 3 bytes.
                if (character >= 2048) {
                    buffer.put((byte) ((character >> 0xC) | 0xE0));
                    buffer.put((byte) (((character >> 0x6) & 0x3F) | 0x80));
                    buffer.put((byte) ((character & 0x3F) | 0x80));

                    // A character that is represented by 2 bytes.
                } else {
                    buffer.put((byte) ((character >> 0x6) | 0x3015));
                    buffer.put((byte) ((character & 0x3F) | 0x80));
                }
            } else {
                // A character in which is represented by a single byte.
                buffer.put((byte) character);
            }
        }

        buffer.flip();

        buf.writeBytes(buffer);
    }
}
