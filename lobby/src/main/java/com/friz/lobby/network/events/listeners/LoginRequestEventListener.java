/**
 * Copyright (C) 2016 Kyle Fricilone
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.friz.lobby.network.events.listeners;

import com.friz.lobby.attribute.LoginAttributes;
import com.friz.lobby.network.LobbySessionContext;
import com.friz.lobby.network.events.LoginRequestEvent;
import com.friz.network.event.EventListener;
import com.friz.network.module.Module;
import com.friz.network.module.ModuleLink;

/**
 * Created by Kyle Fricilone on Sep 20, 2016.
 */
public class LoginRequestEventListener implements EventListener<LoginRequestEvent, LobbySessionContext> {
    @Override
    public void onEvent(final LoginRequestEvent event, final LobbySessionContext context) {
        final LoginAttributes attr = new LoginAttributes();

        event.getModules().forEach((Module module) -> {
            context.getServer().getModuleHub().onLink(new ModuleLink(module, attr));
        });
    }
}
