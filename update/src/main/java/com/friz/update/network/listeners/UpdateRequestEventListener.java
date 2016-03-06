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
import com.friz.update.network.events.UpdateRequestEvent;
import com.friz.update.network.events.UpdateResponseEvent;


/**
 * Created by Kyle Fricilone on 9/20/2015.
 */
public final class UpdateRequestEventListener implements EventListener<UpdateRequestEvent, UpdateSessionContext> {

	@Override
	public void onEvent(UpdateRequestEvent version, UpdateSessionContext context) {
		if (version.getVersion() == 865) {
			context.writeSuccess(UpdateResponseEvent.STATUS_OK);
		} else {
			context.writeFailure(UpdateResponseEvent.STATUS_OUT_OF_DATE);
		}
	}

}
