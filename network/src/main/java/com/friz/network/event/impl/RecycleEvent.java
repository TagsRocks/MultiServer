package com.friz.network.event.impl;

import com.friz.network.event.Event;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Kyle Fricilone on 2/1/2016.
 */
public class RecycleEvent implements Event {

    private final Object obj;

    public RecycleEvent(Object o) {
        this.obj = o;
    }

    public final Object getObj() {
        if (obj instanceof ByteBuf) {
            return Unpooled.copiedBuffer((ByteBuf) obj);
        }
        return obj;
    }

}
