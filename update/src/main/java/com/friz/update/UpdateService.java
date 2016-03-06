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

package com.friz.update;

import com.friz.update.network.UpdateSessionContext;
import com.google.common.util.concurrent.AbstractService;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kyle Fricilone on 9/20/2015.
 */
public class UpdateService extends AbstractService implements Runnable {

    /**
     * The {@link java.util.concurrent.ExecutorService} set with the amount of available processors the computer has.
     */
    private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * A {@link java.util.concurrent.BlockingDeque} of pending {@link com.friz.update.network.UpdateSessionContext}s.
     */
    private final BlockingDeque<UpdateSessionContext> pendingContexts = new LinkedBlockingDeque<>();

    /**
     * The {@link java.util.concurrent.atomic.AtomicBoolean} for the running of the service.
     */
    private final AtomicBoolean running = new AtomicBoolean(true);

    @Override
    protected void doStart() {
        service.submit(this);
    }

    @Override
    public void run() {
        while(running.get()) {
            try {
                UpdateSessionContext context = pendingContexts.take();
                context.processFileQueue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doStop() {
        running.set(false);
        service.shutdownNow();
    }

    /**
     * Adds a {@link com.friz.update.network.UpdateSessionContext} to the {@code pendingContexts}.
     * @param session THe {@link com.friz.update.network.UpdateSessionContext} to registerLobbyPlayer.
     */
    public void addUpdateContext(UpdateSessionContext session) {
        pendingContexts.add(session);
    }
}
