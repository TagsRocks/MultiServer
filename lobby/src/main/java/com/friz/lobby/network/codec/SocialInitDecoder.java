package com.friz.lobby.network.codec;

import com.friz.lobby.network.events.SocialInitRequestEvent;
import com.friz.network.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Kyle Fricilone on 1/31/2016.
 */
public class SocialInitDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int size = buf.readUnsignedShort();
        if (!buf.isReadable(size))
            return;

        int major = buf.readInt();
        int minor = buf.readInt();

        int rsaSize = buf.readUnsignedShort();
        byte[] rsa = new byte[rsaSize];
        buf.readBytes(rsa);

        ByteBuf rsaBuf = Unpooled.wrappedBuffer(new BigInteger(rsa).modPow(Constants.LOGIN_EXPONENT, Constants.LOGIN_MODULUS).toByteArray());
        int rsaMagic = rsaBuf.readUnsignedByte();

        int[] key = new int[4];
        for (int i = 0; i < key.length; i++)
            key[i] = rsaBuf.readInt();

        int type = rsaBuf.readUnsignedByte();
        int aByte1 = rsaBuf.readUnsignedByte();
        int anInt = rsaBuf.readInt();

        int[] ints = new int[5];
        for (int i = 0; i < ints.length; i++)
            ints[i] = rsaBuf.readInt();

        long aLong = rsaBuf.readLong();
        int aByte2 = rsaBuf.readUnsignedByte();
        int aByte3 = rsaBuf.readUnsignedByte();

        out.add(new SocialInitRequestEvent(type, key));
    }

}
