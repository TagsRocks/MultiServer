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

import com.friz.game.GameServer;
import com.friz.login.LoginServer;
import com.friz.update.UpdateServer;

/**
 * Created by Kyle Fricilone on 9/8/2015.
 */
public class MasterServer {

    public static void main(String[] args) {
        UpdateServer u = new UpdateServer();
        u.initialize();
        u.bind();
        System.out.println("UpdateServer initialized on: " + u.getAddress());

        LoginServer l = new LoginServer();
        l.initialize();
        l.bind();
        System.out.println("LoginServer initialized on: " + l.getAddress());

        GameServer g = new GameServer();
        g.initialize();
        g.bind();
        System.out.println("GameServer initialized on: " + g.getAddress());

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.out.println("hook");
                u.stop();
                l.stop();
                g.stop();
            }

        });
    }

}
