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
package com.friz.attribute;

import java.util.Map;

/**
 * Created by Kyle Fricilone on Sep 20, 2016.
 */
public class Attributes {

    private final Map<String, Object> attr = null;

    public final void put(final String ident, final Object obj)
    {
        attr.put(ident, obj);
    }

    public final <T> T get(final String ident, Class<T> type)
    {
        Object obj = attr.get(ident);
        return type.cast(obj);
    }

    public static final void put(final Attributes src, final Attributes dest)
    {
        dest.attr.putAll(src.attr);
    }

}
