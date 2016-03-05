package com.friz.lobby.network.codec;

import com.friz.lobby.network.events.SocialSeedResponseEvent;
import com.friz.network.utility.XTEA;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.security.SecureRandom;

/**
 * Created by Kyle Fricilone on 1/31/2016.
 */
public class SocialSeedEncoder extends MessageToByteEncoder<SocialSeedResponseEvent> {

    @Override
    protected void encode(ChannelHandlerContext ctx, SocialSeedResponseEvent msg, ByteBuf out) throws Exception {
        ByteBuf buf = Unpooled.buffer();

        SecureRandom random = new SecureRandom();

        buf.writeLong(random.nextLong());
        buf.writeLong(random.nextLong());

        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        byte[] xtea = new XTEA(bytes).encrypt(msg.getKeys()).toByteArray();

        out.writeByte(1);
        out.writeShort(xtea.length);
        out.writeBytes(xtea);
    }

}
