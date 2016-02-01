package com.friz.lobby.network.codec;

import com.friz.lobby.network.events.LobbyInitResponseEvent;
import com.friz.network.utility.BufferUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Kyle Fricilone on 1/31/2016.
 */
public class SocialInitEncoder extends MessageToByteEncoder<LobbyInitResponseEvent> {

    @Override
    protected void encode(ChannelHandlerContext ctx, LobbyInitResponseEvent msg, ByteBuf out) throws Exception {
        System.out.println("encode");
        ByteBuf buf = Unpooled.buffer();
        BufferUtils.putJagString(buf, "https://secure.runescape.com/m=sn-integration/google/gamelogin.ws?token=-90759135507956812");

        out.writeShort(buf.readableBytes());
        out.writeBytes(buf);
    }

}
