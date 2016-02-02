package com.friz.network.event.impl;

import com.friz.network.NetworkServer;
import com.friz.network.SessionContext;
import com.friz.network.event.EventListener;
import com.friz.network.event.impl.RecycleEvent;

/**
 * Created by Kyle Fricilone on 2/1/2016.
 */
public class RecycleEventListener implements EventListener<RecycleEvent, SessionContext<NetworkServer>> {

    @Override
    public void onEvent(RecycleEvent event, SessionContext<NetworkServer> context) {
        context.getChannel().pipeline().firstContext().fireChannelRead(event.getObj());
    }

}
