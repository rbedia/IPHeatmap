/**
 * Copyright (C) 2011 Rafael Bedia
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * http://www.gnu.org/copyleft/gpl.html
 */
package org.trillinux.ipheatmap.common;

import java.awt.Point;

/**
 * Utility class for converting from a Hilbert value to an (x,y) coordinate.
 * 
 * @author Rafael Bedia
 */
public final class Hilbert {

    /**
     * Private constructor.
     */
    private Hilbert() {
    }

    /**
     * Figure 14-5 from Hacker's Delight (by Henry S. Warren, Jr. published by
     * Addison Wesley, 2002)
     * 
     * See also http://www.hackersdelight.org/permissions.htm
     */
    public static Point getPoint(long s, int n) {
        long state, x, y;

        state = 0; /* Initialize. */
        x = y = 0;

        for (int i = 2 * n - 2; i >= 0; i -= 2) { /* Do n times. */
            long row = 4 * state | ((s >> i) & 3); /* Row in table. */
            x = (x << 1) | ((0x936C >> row) & 1);
            y = (y << 1) | ((0x39C6 >> row) & 1);
            state = (0x3E6B94C1 >> 2 * row) & 3; /* New state. */
        }

        return new Point((int) x, (int) y);
    }

}
