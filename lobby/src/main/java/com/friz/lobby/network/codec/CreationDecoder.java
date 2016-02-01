package com.friz.lobby.network.codec;

import com.friz.network.Constants;
import com.friz.network.utility.BufferUtils;
import com.friz.network.utility.XTEA;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Kyle Fricilone on 1/23/2016.
 */
public class CreationDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int size = buf.readUnsignedShort();
        if (!buf.isReadable(size))
            return;

        int major = buf.readUnsignedShort();
        int minor = buf.readUnsignedShort();

        int rsaSize = buf.readUnsignedShort();
        byte[] rsa = new byte[rsaSize];
        buf.readBytes(rsa);

        ByteBuf rsaBuf = Unpooled.wrappedBuffer(new BigInteger(rsa).modPow(Constants.LOGIN_EXPONENT, Constants.LOGIN_MODULUS).toByteArray());
        int rsaMagic = rsaBuf.readUnsignedByte();

        int[] key = new int[4];
        for (int i = 0; i < key.length; i++)
            key[i] = rsaBuf.readInt();

        int[] ints = new int[10];
        for (int i = 0; i < ints.length; i++)
            ints[i] = rsaBuf.readInt();

        int aShort = rsaBuf.readUnsignedShort();

        byte[] xtea = new byte[buf.readableBytes()];
        buf.readBytes(xtea);
        ByteBuf xteaBuf = Unpooled.wrappedBuffer(new XTEA(xtea).decrypt(key).toByteArray());

        String token = BufferUtils.getString(xteaBuf);
        int aShort2 = xteaBuf.readUnsignedShort();
        int anInt = xteaBuf.readInt();
        int anInt2 = xteaBuf.readInt();
        String seed = BufferUtils.getString(xteaBuf);
        int aByte = xteaBuf.readUnsignedByte();
        int aByte2 = xteaBuf.readUnsignedByte();

        byte[] uid = new byte[24];
        for (int i = 0; i < uid.length; i++)
            uid[i] = xteaBuf.readByte();

        boolean hasAdditional = xteaBuf.readBoolean();
        String additionalInfo = "";
        if (hasAdditional)
            additionalInfo = BufferUtils.getString(xteaBuf);

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
        int anInt3 = xteaBuf.readUnsignedByte();
        int anInt4 = xteaBuf.readInt();
        int anInt5 = xteaBuf.readInt();
        int anInt6 = xteaBuf.readInt();
        int anInt7 = xteaBuf.readInt();
        String aString2 = BufferUtils.getJagString(xteaBuf);
        System.out.println(seed);
    }

}
