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

package com.friz.update.network.events;


import com.friz.network.event.Event;

/**
 * Created by Kyle Fricilone on 9/20/2015.
 */
public final class UpdateStatusMessageEvent implements Event {

	public static final int STATUS_OK = 0;
	public static final int STATUS_OUT_OF_DATE = 6;
	public static final int STATUS_SERVER_FULL = 7;

	private final int status;

	public UpdateStatusMessageEvent(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
