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

package com.friz.lobby.network.listeners;

import com.friz.lobby.network.LobbySessionContext;
import com.friz.lobby.network.codec.*;
import com.friz.lobby.network.events.LobbyInitRequestEvent;
import com.friz.lobby.network.events.LobbyInitResponseEvent;
import com.friz.network.event.EventListener;
import io.netty.channel.ChannelFuture;

/**
 * Created by Kyle Fricilone on 9/18/2015.
 */
public class LobbyInitEventListener implements EventListener<LobbyInitRequestEvent, LobbySessionContext> {

    @Override
    public void onEvent(LobbyInitRequestEvent event, LobbySessionContext context) {
        if (event.getType() == 14) {
            context.write(new LobbyInitResponseEvent(0)).addListener((ChannelFuture future) -> {
                if (future.isSuccess()) {
                    context.getChannel().pipeline().remove(LobbyInitDecoder.class.getName());
                    context.getChannel().pipeline().remove(LobbyInitEncoder.class.getName());
                    context.getChannel().pipeline().addFirst(LoginDecoder.class.getName(), new LoginDecoder());
                    context.getChannel().pipeline().addFirst(LoginEncoder.class.getName(), new LoginEncoder());
                }
            });
        } else if (event.getType() == 28) {
            context.getChannel().pipeline().remove(LobbyInitDecoder.class.getName());
            context.getChannel().pipeline().remove(LobbyInitEncoder.class.getName());
            context.getChannel().pipeline().addFirst(CreationDecoder.class.getName(), new CreationDecoder());
            context.getChannel().pipeline().addFirst(CreationEncoder.class.getName(), new CreationEncoder());
        } else if (event.getType() == 29) {
            context.write(new LobbyInitResponseEvent(0)).addListener((ChannelFuture future) -> {
                if (future.isSuccess()) {
                    context.getChannel().pipeline().remove(LobbyInitDecoder.class.getName());
                    context.getChannel().pipeline().remove(LobbyInitEncoder.class.getName());
                    context.getChannel().pipeline().addFirst(SocialInitDecoder.class.getName(), new SocialInitDecoder());
                    context.getChannel().pipeline().addFirst(SocialInitEncoder.class.getName(), new SocialInitEncoder());
                }
            });
        }
    }
}
