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
public final class FileRequestEvent implements Event {

	/**
	 * The priority of the file requested.
	 */
	private final boolean priority;
	
	/**
	 * The type of file and the file that is requested.
	 */
	private final int type, file;

	/**
	 * Creates a new FileRequest.
	 * @param priority The priority of the file.
	 * @param type The type of file.
	 * @param file The file id requested.
	 */
	public FileRequestEvent(boolean priority, int type, int file) {
		this.priority = priority;
		this.type = type;
		this.file = file;
	}

	/**
	 * Gets the file id.
	 * @return The file.
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Gets the type of file.
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Is the file a priority or not.
	 * @return The priority.
	 */
	public boolean isPriority() {
		return priority;
	}
}
