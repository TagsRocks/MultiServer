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
package com.friz.lobby.network.modules.listeners;

import com.friz.lobby.attribute.LoginAttributes;
import com.friz.lobby.network.modules.ClientVersionModule;
import com.friz.network.Constants;
import com.friz.network.module.ModuleListener;

/**
 * Created by Kyle Fricilone on Sep 20, 2016.
 */
public class ClientVersionModuleListener implements ModuleListener<ClientVersionModule, LoginAttributes>{
    @Override
    public void onModule(final ClientVersionModule module, final LoginAttributes attr) {
        if (Constants.MAJOR != module.getMajorVersion()) {
            //kill connection
        }

        if (Constants.MINOR != module.getMinorVersion()) {
            //kill connection
        }

        attr.put("majorVersion", module.getMajorVersion());
        attr.put("minorVersion", module.getMinorVersion());
    }
}
