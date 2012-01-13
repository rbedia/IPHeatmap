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
 * Representation of a CIDR block.
 * 
 * @author Rafael Bedia
 */
public class CIDR {

    public static final long MAX_IP = 0xFFFFFFFFl;

    private long start;

    private long end;

    private int mask;

    /**
     * Default constructor.
     */
    public CIDR() {
    }

    /**
     * Constructs a CIDR object by deep copying an existing CIDR object.
     * 
     * @param cidr
     */
    public CIDR(CIDR cidr) {
        start = cidr.start;
        end = cidr.end;
        mask = cidr.mask;
    }

    /**
     * Constructs a CIDR object by using the specified parameters.
     * 
     * @param start
     * @param end
     * @param mask
     */
    public CIDR(long start, long end, int mask) {
        this.start = start;
        this.mask = mask;
        this.end = end;
    }

    /**
     * Constructs a CIDR object by parsing a CIDR format string.
     * 
     * @param input
     */
    public CIDR(String input) {
        String[] parts = input.split("/");

        int ip = IPUtil.ipToInt(parts[0]);
        mask = Integer.parseInt(parts[1]);
        start = ip & MAX_IP;

        long maskBits = MAX_IP;
        end = ip & MAX_IP | (maskBits >> mask);
    }

    /**
     * Returns true if cidr overlaps with this object.
     * 
     * @param cidr
     *            the CIDR block to check for overlap
     * @return true if cidr overlaps with this object
     */
    public boolean overlaps(CIDR cidr) {
        return (start <= cidr.start && cidr.start <= end)
                || (start <= cidr.end && cidr.end <= end)
                || (cidr.start <= start && start <= cidr.end)
                || (cidr.start <= end && end <= cidr.end);
    }

    /**
     * Gets this object in CIDR text format.
     * 
     * @return this object in CIDR text format
     */
    public String getText() {
        return IPUtil.intToIp((int) start) + "/" + mask;
    }

    /**
     * Returns a hashcode value for the object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (end ^ (end >>> 32));
        result = prime * result + mask;
        result = prime * result + (int) (start ^ (start >>> 32));
        return result;
    }

    /**
     * Compares the object for equality. All fields are checked.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CIDR other = (CIDR) obj;
        if (end != other.end) {
            return false;
        }
        if (mask != other.mask) {
            return false;
        }
        if (start != other.start) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return getText();
    }

    /**
     * @return the start
     */
    public long getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public long getEnd() {
        return end;
    }

    /**
     * @param end
     *            the end to set
     */
    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * @return the mask
     */
    public int getMask() {
        return mask;
    }

    /**
     * @param mask
     *            the mask to set
     */
    public void setMask(int mask) {
        this.mask = mask;
    }

}
