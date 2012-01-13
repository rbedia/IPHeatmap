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
package org.trillinux.ipheatmap.common.list;

/**
 * 
 * @author Rafael Bedia
 */
public class GroupCount {

    private long subnet;

    private int hostbits;

    private int count;

    /**
     * @param subnet
     * @param mask
     * @param count
     */
    public GroupCount(long subnet, int hostbits, int count) {
        this.subnet = subnet;
        this.hostbits = hostbits;
        this.count = count;
    }

    /**
     * @return the subnet
     */
    public long getSubnet() {
        return subnet;
    }

    /**
     * @param subnet
     *            the subnet to set
     */
    public void setSubnet(long subnet) {
        this.subnet = subnet;
    }

    /**
     * @return the hostbits
     */
    public int getHostbits() {
        return hostbits;
    }

    /**
     * @param hostbits
     *            the hostbits to set
     */
    public void setHostbits(int hostbits) {
        this.hostbits = hostbits;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

}
