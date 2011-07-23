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
	public long start;
	
	public long end;
	
	public int mask;
	
	
	public CIDR() {
	}
	
	public CIDR(CIDR cidr) {
		start = cidr.start;
		end = cidr.end;
		mask = cidr.mask;
	}

	public CIDR(long start, long end, int mask) {
		this.start = start;
		this.mask = mask;
		this.end = end;
	}
	
	public CIDR(String input) {
		String[] parts = input.split("/");
		
		int ip = IPUtil.ipToInt(parts[0]);
		mask = Integer.parseInt(parts[1]);
		start = ip & 0xFFFFFFFFl;
		
		long maskBits = 0xFFFFFFFFl;
		end = ip & 0xFFFFFFFFl | (maskBits >> mask);
	}

	public boolean overlaps(CIDR cidr) {
		return (start <= cidr.start && cidr.start <= end) ||
				(start <= cidr.end && cidr.end <= end) ||
				(cidr.start <= start && start <= cidr.end) || 
				(cidr.start <= end && end <= cidr.end);
	}

	public String getText() {
		return IPUtil.intToIp((int) start) + "/" + mask;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (end ^ (end >>> 32));
		result = prime * result + mask;
		result = prime * result + (int) (start ^ (start >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CIDR other = (CIDR) obj;
		if (end != other.end)
			return false;
		if (mask != other.mask)
			return false;
		if (start != other.start)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getText();
	}
	
}
