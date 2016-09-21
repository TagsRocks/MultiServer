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
package com.friz.lobby.network.modules;

import com.friz.network.module.Module;

/**
 * Created by Kyle Fricilone on Sep 20, 2016.
 */
public class ClientTypeModule implements Module {

    private final int gameType, langType, displayType, widthType, heightType;


    public ClientTypeModule(final int gameType, final int langType, final int displayType, final int widthType, final int heightType) {
        this.gameType = gameType;
        this.langType = langType;
        this.displayType = displayType;
        this.widthType = widthType;
        this.heightType = heightType;
    }

    public final int getGameType() {
        return gameType;
    }

    public final int getLangType() {
        return langType;
    }

    public final int getDisplayType() {
        return displayType;
    }

    public final int getWidthType() {
        return widthType;
    }

    public final int getHeightType() {
        return heightType;
    }
}
