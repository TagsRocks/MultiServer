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

package com.friz.lobby.network.events;

/**
 * Created by Kyle Fricilone on 9/24/2015.
 */
public class SocialInitResponseEvent {

    private final int type;
    private final int hash;
    private final int[] keys;

    public SocialInitResponseEvent(int type, int hash, int[] keys) {
        this.type = type;
        this.hash = hash;
        this.keys = keys;
    }

    public final int getType() {
        return type;
    }

    public final int getHash() { return hash; }

    public final int[] getKeys() {
        return keys;
    }
}
