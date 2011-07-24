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

/**
 * Utility class for converting between integers and dotted decimal text format
 * of an IP address.
 * 
 * @author Rafael Bedia
 */
public class IPUtil {

    /**
     * Converts the text form of an IP address to an integer.
     * 
     * @param addr
     * @return
     */
    public static int ipToInt(final String addr) {
        final String[] addressBytes = addr.split("\\.");

        if (addressBytes.length != 4) {
            throw new IllegalArgumentException("IPs have the format x.x.x.x");
        }

        int ip = 0;
        for (int i = 0; i < 4; i++) {
            ip <<= 8;
            ip |= Integer.parseInt(addressBytes[i]);
        }
        return ip;
    }

    /**
     * Converts an integer IP address into its text form.
     * 
     * @param i
     * @return
     */
    public static String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }
}
