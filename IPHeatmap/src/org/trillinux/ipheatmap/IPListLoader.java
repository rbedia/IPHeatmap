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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IPListLoader {
	File ipDir;

	public IPListLoader(File ipDir) {
		this.ipDir = ipDir;
	}
	
	public List<IPMapping> getMappings() {
		List<IPMapping> mappings = new ArrayList<IPMapping>();
		searchDirectory(ipDir, mappings);
		return mappings;
	}
	
	public void searchDirectory(File file, List<IPMapping> mappings) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				searchDirectory(child, mappings);
			}
		} else {
			CIDR cidr = CIDR.cidr_parse(file.getName().replace('-', '/'));
			mappings.add(new IPMapping(cidr, file));
		}
	}
}
