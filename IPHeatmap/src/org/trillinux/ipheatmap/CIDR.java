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
package org.trillinux.ipheatmap;

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

	public static CIDR cidr_parse(String input) {
		CIDR cidr = new CIDR();
		
		String[] parts = input.split("/");
		
		int ip = IPUtil.ipToLong(parts[0]);
		cidr.mask = Integer.parseInt(parts[1]);
		cidr.start = ip;
		
		long maskBits = 0xFFFFFFFFl;
		cidr.end = ((long) ip) | (maskBits >> cidr.mask);
		
		return cidr;
	}

	public static CIDR cidr_parse(long start, long end, int mask) {
		CIDR cidr = new CIDR();
		cidr.start = start;
		cidr.mask = mask;
		cidr.end = end;
		return cidr;
	}
	
	public boolean overlaps(CIDR cidr) {
		return (start <= cidr.start && cidr.start <= end) ||
			(start <= cidr.end && cidr.end <= end);
	}

	public String getText() {
		return IPUtil.intToIp((int) start) + "/" + mask;
	}

	@Override
	public String toString() {
		return getText();
	}
	
}
