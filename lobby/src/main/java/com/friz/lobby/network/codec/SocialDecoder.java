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

package com.friz.lobby.network.codec;

import com.friz.network.Constants;
import com.friz.network.utility.BufferUtils;
import com.friz.network.utility.XTEA;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kyle Fricilone on 9/22/2015.
 */
public class SocialDecoder extends ByteToMessageDecoder {

    private final int[] key;

    public SocialDecoder(int[] key) {
        this.key = key;
        System.out.println(Arrays.toString(key));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        if (!buf.isReadable())
            return;

        int type = buf.readUnsignedByte();
        int size = buf.readUnsignedShort();

        if (!buf.isReadable(size))
            return;

        byte[] xtea = new byte[size];
        buf.readBytes(xtea);
        ByteBuf xteaBuf = Unpooled.wrappedBuffer(new XTEA(xtea).decrypt(key).toByteArray());

        int game = xteaBuf.readUnsignedByte();
        int lang = xteaBuf.readUnsignedByte();
        int display = xteaBuf.readUnsignedByte();
        int width = xteaBuf.readUnsignedShort();
        int height = xteaBuf.readUnsignedShort();

        int multisample = xteaBuf.readByte();

        byte[] uid = new byte[24];
        for (int i = 0; i < uid.length; i++)
            uid[i] = xteaBuf.readByte();

        String gameToken = BufferUtils.getString(xteaBuf);

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
        xteaBuf.readMedium();
        xteaBuf.readMedium();

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
        String gpuName = BufferUtils.getJagString(xteaBuf);
        String aString = BufferUtils.getJagString(xteaBuf);
        String dxVersion = BufferUtils.getJagString(xteaBuf);
        String aString1 = BufferUtils.getJagString(xteaBuf);
        int gpuDriverMonth = xteaBuf.readUnsignedByte();
        int gpuDriverYear = xteaBuf.readUnsignedShort();
        String cpuType = BufferUtils.getJagString(xteaBuf);
        String cpuName = BufferUtils.getJagString(xteaBuf);
        int cpuThreads = xteaBuf.readUnsignedByte();
        int anInt = xteaBuf.readUnsignedByte();
        int anInt1 = xteaBuf.readInt();
        int anInt2 = xteaBuf.readInt();
        int anInt3 = xteaBuf.readInt();
        int anInt4 = xteaBuf.readInt();
        String aString2 = BufferUtils.getJagString(xteaBuf);

        int anInt5 = xteaBuf.readInt();
        String seed = BufferUtils.getString(xteaBuf);
        int affiliate = xteaBuf.readInt();
        int anInt6 = xteaBuf.readInt();
        String updateToken = BufferUtils.getString(xteaBuf);
        int anInt7 = xteaBuf.readUnsignedByte();

        int[] checksums = new int[(xteaBuf.readableBytes() / 4) + 1];
        for (int i = 0; i < checksums.length; i++) {
            if (i == 32)
                checksums[i] = -1;
            else
                checksums[i] = xteaBuf.readInt();
        }

        System.out.println(seed);
    }
}
