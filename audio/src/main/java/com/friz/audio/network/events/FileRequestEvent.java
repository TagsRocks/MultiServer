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

package com.friz.audio.network.events;


import com.friz.network.event.Event;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Created by Kyle Fricilone on 9/20/2015.
 */
public final class FileRequestEvent implements Event {

	/**
	 * The type of file and the file that is requested.
	 */
	private final int type, file, crc, version;

	/**
	 * The http version of the request
	 */
	private final HttpVersion http;

	/**
	 * Creates a new FileRequest.
	 * @param type The type of file.
	 * @param file The file id requested.
	 */
	public FileRequestEvent(int type, int file, int crc, int version, HttpVersion http) {
		this.type = type;
		this.file = file;
		this.crc = crc;
		this.version = version;
		this.http = http;
	}

	/**
	 * Gets the type of file.
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the file id.
	 * @return The file.
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Gets the crc of file.
	 * @return The crc.
	 */
	public int getCrc() {
		return crc;
	}

	/**
	 * Gets the version of file.
	 * @return The version.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Gets the http version of file.
	 * @return The http version.
	 */
	public HttpVersion getHttp() {
		return http;
	}
}
