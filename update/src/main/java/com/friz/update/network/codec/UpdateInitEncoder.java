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

package com.friz.update.network.codec;

import com.friz.update.network.events.UpdateResponseEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Kyle Fricilone on 9/24/2015.
 */
public class UpdateInitEncoder extends MessageToByteEncoder<UpdateResponseEvent> {

    /**
     * Delta integers for the loading screen percentages.
     */
    public static final int[] PERCENT_DELTA = { 2669,69795,41651,35866,358716,44375,18239,20677,169903,1095188,425011,554838,814467,1150563,34870,820648,13,20549,1244,65358,2109,119,1351461,4173641,8991,23087 };


    @Override
    protected void encode(ChannelHandlerContext ctx, UpdateResponseEvent msg, ByteBuf out) throws Exception {
        out.writeByte(msg.getStatus());
        if (msg.getStatus() == UpdateResponseEvent.STATUS_OK) {
            for (int i = 0; i < PERCENT_DELTA.length; i++) {
                out.writeInt(PERCENT_DELTA[i]);
            }
        }
    }

}
