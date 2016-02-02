package com.friz.lobby.network.codec;

import com.friz.lobby.network.events.SocialInitResponseEvent;
import com.friz.network.utility.BufferUtils;
import com.friz.network.utility.XTEA;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Kyle Fricilone on 1/31/2016.
 */
public class SocialInitEncoder extends MessageToByteEncoder<SocialInitResponseEvent> {

    @Override
    protected void encode(ChannelHandlerContext ctx, SocialInitResponseEvent msg, ByteBuf out) throws Exception {
        ByteBuf buf = Unpooled.buffer();

        String host = "https://secure.runescape.com/m=sn-integration/";
        if (msg.getType() == 0) {
            host += "google/";
        } else {
            host += "facebook/";
        }

        BufferUtils.putJagString(buf, host + "gamelogin.ws?token=" + msg.getHash());

        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        byte[] xtea = new XTEA(bytes).encrypt(msg.getKeys()).toByteArray();

        out.writeShort(xtea.length);
        out.writeBytes(xtea);
    }

}
