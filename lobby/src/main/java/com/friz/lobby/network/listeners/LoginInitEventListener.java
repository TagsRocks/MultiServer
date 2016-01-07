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
import com.friz.lobby.network.codec.LoginDecoder;
import com.friz.lobby.network.codec.LoginEncoder;
import com.friz.lobby.network.codec.LoginInitDecoder;
import com.friz.lobby.network.codec.LoginInitEncoder;
import com.friz.lobby.network.events.LoginInitRequestEvent;
import com.friz.lobby.network.events.LoginInitResponseEvent;
import com.friz.network.event.EventListener;
import io.netty.channel.ChannelFuture;

/**
 * Created by Kyle Fricilone on 9/18/2015.
 */
public class LoginInitEventListener implements EventListener<LoginInitRequestEvent, LobbySessionContext> {

    @Override
    public void onEvent(LoginInitRequestEvent event, LobbySessionContext context) {
        if (event.getType() == 14) {
            context.write(new LoginInitResponseEvent(0)).addListener((ChannelFuture future) -> {
                if (future.isSuccess()) {
                    context.getChannel().pipeline().remove(LoginInitDecoder.class.getName());
                    context.getChannel().pipeline().remove(LoginInitEncoder.class.getName());
                    context.getChannel().pipeline().addLast(LoginEncoder.class.getName(), new LoginEncoder());
                    context.getChannel().pipeline().addLast(LoginDecoder.class.getName(), new LoginDecoder());
                }
            });
        }
    }
}
