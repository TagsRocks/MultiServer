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

package com.friz.update.network.listeners;

import com.friz.network.event.EventListener;
import com.friz.update.network.UpdateSessionContext;
import com.friz.update.network.events.UpdateStatusMessageEvent;
import com.friz.update.network.events.ValidationMessageEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;


/**
 * Created by Kyle Fricilone on 9/20/2015.
 */
public final class ValidationEventListener implements EventListener<ValidationMessageEvent, UpdateSessionContext> {

	@Override
	public void onEvent(ValidationMessageEvent version, UpdateSessionContext context) {

        int status;

		if (version.getVersion() == 855) {
			status = UpdateStatusMessageEvent.STATUS_OK;
		} else {
			status = UpdateStatusMessageEvent.STATUS_OUT_OF_DATE;
		}

		Channel channel = context.getChannel();
		ChannelFuture future = channel.writeAndFlush(new UpdateStatusMessageEvent(status));
		if (status == UpdateStatusMessageEvent.STATUS_OK) {
			context.setHandshakeComplete(true);
		} else {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

}
