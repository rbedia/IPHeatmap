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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the mappings from CIDR blocks to files within a directory. An index
 * file is read so that the whole directory does not need to be scanned.
 * 
 * @author Rafael Bedia
 *
 */
public class IPListLoader {
	File ipDir;

	public IPListLoader(File ipDir) {
		this.ipDir = ipDir;
	}
	
	public List<IPMapping> getMappings() {
		return readIndex();
	}
	
	public List<IPMapping> readIndex() {
		List<IPMapping> mappings = new ArrayList<IPMapping>();
		try {
			FileInputStream fileIn = new FileInputStream(new File(ipDir, "index.txt"));
			DataInputStream in = new DataInputStream(fileIn);
			
			try {
				while(true) {
					long start = in.readLong();
					long end = in.readLong();
					int mask = in.readInt();
					CIDR cidr = CIDR.cidr_parse(start, end, mask);
					String filePath = in.readUTF();
					mappings.add(new IPMapping(cidr, new File(ipDir, filePath)));
				}
			} catch (EOFException ex) {
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mappings;
	}
}
