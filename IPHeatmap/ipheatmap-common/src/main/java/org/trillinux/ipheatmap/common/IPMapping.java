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

import java.io.File;

/**
 * Maps a CIDR range to a file containing a list of IPs that fall within the
 * CIDR range. The format for the file is one IP per line in ASCII dotted
 * decimal format.
 * 
 * @author Rafael Bedia
 */
public class IPMapping {
	private CIDR range;
	
	private File ipFile;

	public IPMapping(CIDR range, File ipFile) {
		super();
		this.range = range;
		this.ipFile = ipFile;
	}

	public CIDR getRange() {
		return range;
	}

	public File getIpFile() {
		return ipFile;
	}

	@Override
	public String toString() {
		return range + " - " + ipFile;
	}
	
}
