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

package com.friz.master;

import com.friz.cache.Cache;
import com.friz.cache.FileStore;
import com.friz.game.GameServer;
import com.friz.lobby.LobbyServer;
import com.friz.login.LoginServer;
import com.friz.network.Constants;
import com.friz.update.UpdateServer;

import java.io.IOException;

/**
 * Created by Kyle Fricilone on 9/8/2015.
 */
public class MasterServer {

    public static void main(String[] args) throws Exception {
        Cache cache = new Cache(FileStore.open(Constants.CACHE_LOCATION));
        cache.initialize();
        System.out.println("Cache Initialized " + cache.getTypeCount() + " Index(es)");

        UpdateServer updateServer = new UpdateServer(cache);
        updateServer.initialize();
        updateServer.bind();
        System.out.println("UpdateServer initialized on: " + updateServer.getAddress());

        LoginServer loginServer = new LoginServer();
        loginServer.initialize();
        loginServer.bind();
        System.out.println("LoginServer initialized on: " + loginServer.getAddress());

        LobbyServer lobbyServer = new LobbyServer(cache);
        lobbyServer.initialize();
        lobbyServer.bind();
        System.out.println("LobbyServer initialized on: " + lobbyServer.getAddress());

        GameServer gameServer = new GameServer(cache);
        gameServer.initialize();
        gameServer.bind();
        System.out.println("GameServer initialized on: " + gameServer.getAddress());

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.out.println("hook");
                try {
                    cache.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    updateServer.stop();
                    loginServer.stop();
                    gameServer.stop();
                }
            }

        });
    }

}
