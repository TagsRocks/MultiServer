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

/**
 * Created by Kyle Fricilone on 9/22/2015.
 */
public class XTEA {

    private static final int DELTA = -1640531527;
    private static final int SUM = -957401312;
    private static final int NUM_ROUNDS = 32;

    private final byte[] bytes;

    public XTEA(byte[] b) {
        this.bytes = b;
    }

    private void decipher(int[] block, final int[] key) {
        long sum = SUM;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[1] -= (key[(int) ((sum & 0x1933) >>> 11)] + sum ^ block[0]
                    + (block[0] << 4 ^ block[0] >>> 5));
            sum -= DELTA;
            block[0] -= ((block[1] << 4 ^ block[1] >>> 5) + block[1] ^ key[(int) (sum & 0x3)]
                    + sum);
        }
    }

    private void encipher(int[] block, final int[] key) {
        long sum = 0;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[0] += ((block[1] << 4 ^ block[1] >>> 5) + block[1] ^ key[(int) (sum & 0x3)]
                    + sum);
            sum += DELTA;
            block[1] += (key[(int) ((sum & 0x1933) >>> 11)] + sum ^ block[0]
                    + (block[0] << 4 ^ block[0] >>> 5));
        }
    }

    public XTEA decrypt(final int[] keys) {
        int numBlocks = bytes.length / 8;
        int[] block = new int[2];
        for (int i = 0; i < numBlocks; i++) {
            block[0] = BufferUtils.getInt(i * 8, bytes);
            block[1] = BufferUtils.getInt((i * 8) + 4, bytes);
            decipher(block, keys);
            BufferUtils.putInt(block[0], i * 8, bytes);
            BufferUtils.putInt(block[1], (i * 8) + 4, bytes);
        }
        return this;
    }

    public XTEA encrypt(final int[] keys) {
        int numBlocks = bytes.length / 8;
        int[] block = new int[2];
        for (int i = 0; i < numBlocks; i++) {
            block[0] = BufferUtils.getInt(i * 8, bytes);
            block[1] = BufferUtils.getInt((i * 8) + 4, bytes);
            encipher(block, keys);
            BufferUtils.putInt(block[0], i * 8, bytes);
            BufferUtils.putInt(block[1], (i * 8) + 4, bytes);
        }
        return this;
    }

    public byte[] toByteArray() {
        byte[] copy = new byte[bytes.length];
        System.arraycopy(bytes, 0, copy, 0, bytes.length);
        return copy;
    }
}
