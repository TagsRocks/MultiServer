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

package com.friz.game.network.codec;

import com.friz.network.utility.BufferUtils;
import com.friz.network.utility.XTEA;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Kyle Fricilone on 9/23/2015.
 */
public class LoginDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int type = buf.readUnsignedByte();
        int size = buf.readUnsignedShort();

        int major = buf.readInt();
        int minor = buf.readInt();
        boolean dropped = buf.readBoolean();

        int rsaSize = buf.readUnsignedShort();
        byte[] rsa = new byte[rsaSize];
        buf.readBytes(rsa);

        ByteBuf rsaBuf = Unpooled.wrappedBuffer(new BigInteger(rsa).modPow(BigInteger.ONE, BigInteger.ONE).toByteArray());
        int rsaMagic = rsaBuf.readUnsignedByte();

        int[] key = new int[4];
        for (int i = 0; i < key.length; i++)
            key[i] = rsaBuf.readInt();

        int block = rsaBuf.readUnsignedByte();

        if (block == 1 || block == 3) {
            int code = rsaBuf.readUnsignedMedium();
            rsaBuf.readerIndex(rsaBuf.readerIndex() + 1);
        } else if (block == 0) {
            int trusted = rsaBuf.readInt();
        } else if (block == 2) {
            rsaBuf.readerIndex(rsaBuf.readerIndex() + 4);
        }

        String password = BufferUtils.getString(rsaBuf);

        long serverKey = rsaBuf.readLong();
        long clientKey = rsaBuf.readLong();

        byte[] xtea = new byte[buf.readableBytes()];
        buf.readBytes(xtea);
        ByteBuf xteaBuf = Unpooled.wrappedBuffer(new XTEA(xtea).decrypt(key).toByteArray());

        boolean asBase = xteaBuf.readBoolean();
        if (asBase)
            BufferUtils.getBase37(xteaBuf);
        else
            BufferUtils.getString(xteaBuf);

        int display = xteaBuf.readUnsignedByte();
        int width = xteaBuf.readUnsignedShort();
        int height = xteaBuf.readUnsignedShort();

        int multisample = xteaBuf.readByte();

        byte[] uid = new byte[24];
        for (int i = 0; i < uid.length; i++)
            uid[i] = xteaBuf.readByte();

        String settings = BufferUtils.getString(xteaBuf);

        int prefSize = xteaBuf.readUnsignedByte();
        int prefVersion = xteaBuf.readUnsignedByte();
        int aPref = xteaBuf.readUnsignedByte();
        int antiAliasing = xteaBuf.readUnsignedByte();
        int aPref1 = xteaBuf.readUnsignedByte();
        int bloom = xteaBuf.readUnsignedByte();
        int brightness = xteaBuf.readUnsignedByte();
        int buildArea = xteaBuf.readUnsignedByte();
        int aPref2 = xteaBuf.readUnsignedByte();
        int flickeringEffects = xteaBuf.readUnsignedByte();
        int fog = xteaBuf.readUnsignedByte();
        int groundBlending = xteaBuf.readUnsignedByte();
        int groundDecoration = xteaBuf.readUnsignedByte();
        int idleAnimations = xteaBuf.readUnsignedByte();
        int lighting = xteaBuf.readUnsignedByte();
        int sceneryShadows = xteaBuf.readUnsignedByte();
        int aPref3 = xteaBuf.readUnsignedByte();
        int nullPref = xteaBuf.readUnsignedByte();
        int orthoMode = xteaBuf.readUnsignedByte();
        int particles = xteaBuf.readUnsignedByte();
        int removeRoofs = xteaBuf.readUnsignedByte();
        int maxScreenSize = xteaBuf.readUnsignedByte();
        int skyboxes = xteaBuf.readUnsignedByte();
        int mobShadows = xteaBuf.readUnsignedByte();
        int textures = xteaBuf.readUnsignedByte();
        int desiredToolkit = xteaBuf.readUnsignedByte();
        int nullPref1 = xteaBuf.readUnsignedByte();
        int water = xteaBuf.readUnsignedByte();
        int screenSize = xteaBuf.readUnsignedByte();
        int customCursors = xteaBuf.readUnsignedByte();
        int graphics = xteaBuf.readUnsignedByte();
        int cpu = xteaBuf.readUnsignedByte();
        int aPref4 = xteaBuf.readUnsignedByte();
        int safeMode = xteaBuf.readUnsignedByte();
        int aPref5 = xteaBuf.readUnsignedByte();
        int aPref6 = xteaBuf.readUnsignedByte();
        int aPref7 = xteaBuf.readUnsignedByte();
        int soundEffectsVolume = xteaBuf.readUnsignedByte();
        int areaSoundsVolume = xteaBuf.readUnsignedByte();
        int voiceOverVolume = xteaBuf.readUnsignedByte();
        int musicVolume = xteaBuf.readUnsignedByte();
        int themeMusicVolume = xteaBuf.readUnsignedByte();
        int steroSound = xteaBuf.readUnsignedByte();

        int infoVersion = xteaBuf.readUnsignedByte();
        int osType = xteaBuf.readUnsignedByte();
        boolean arch64 = xteaBuf.readBoolean();
        int versionType = xteaBuf.readUnsignedByte();
        int vendorType = xteaBuf.readUnsignedByte();
        int jMajor = xteaBuf.readUnsignedByte();
        int jMinor = xteaBuf.readUnsignedByte();
        int jPatch = xteaBuf.readUnsignedByte();
        boolean falseBool = xteaBuf.readBoolean();
        int heapSize = xteaBuf.readUnsignedShort();
        int pocessorCount = xteaBuf.readUnsignedByte();
        int cpuPhyscialMemory = xteaBuf.readUnsignedMedium();
        int cpuClock = xteaBuf.readUnsignedShort();
        String gpuName = BufferUtils.getString(xteaBuf);
        String aString = BufferUtils.getString(xteaBuf);
        String dxVersion = BufferUtils.getString(xteaBuf);
        String aString1 = BufferUtils.getString(xteaBuf);
        int gpuDriverMonth = xteaBuf.readUnsignedByte();
        int gpuDriverYear = xteaBuf.readUnsignedShort();
        String cpuType = BufferUtils.getString(xteaBuf);
        String cpuName = BufferUtils.getString(xteaBuf);
        int cpuThreads = xteaBuf.readUnsignedByte();
        int anInt = xteaBuf.readUnsignedByte();
        int anInt1 = xteaBuf.readInt();
        int anInt2 = xteaBuf.readInt();
        int anInt3 = xteaBuf.readInt();
        int anInt4 = xteaBuf.readInt();
        String aString2 = BufferUtils.getString(xteaBuf);

        int anInt5 = xteaBuf.readInt();
        int anInt6 = xteaBuf.readInt();
        int anInt7 = xteaBuf.readInt();
        String aString3 = BufferUtils.getString(xteaBuf);

        boolean hasAdditional = xteaBuf.readBoolean();
        String additionalInfo = new String();
        if (hasAdditional)
            additionalInfo = BufferUtils.getString(xteaBuf);

        int anInt8 = xteaBuf.readUnsignedByte();
        int anInt9 = xteaBuf.readUnsignedByte();
        int anInt10 = xteaBuf.readUnsignedByte();
        int anInt11 = xteaBuf.readInt();
        String aString4 = BufferUtils.getString(xteaBuf);

        boolean newWorld = xteaBuf.readBoolean();
        int lobbyId = xteaBuf.readUnsignedShort();

        int[] checksums = new int[xteaBuf.readableBytes() / 4];
        for (int i = 0; i < checksums.length; i++)
            checksums[i] = xteaBuf.readInt();
    }

}
