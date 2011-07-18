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
package org.trillinux.ipheatmap.server;

import java.io.File;

/**
 * Configuration for a tile server.
 * 
 * @author Rafael Bedia
 */
public class TileServletConfig {

	private File ipDir;
	private File cacheDir;
	private String name;
	private boolean cache = true;
	
	/**
	 * 
	 * @param ipDir the directory that stores IP lists
	 * @param cacheDir the directory where tiles will be cached
	 * @param name the name of the tile set
	 */
	public TileServletConfig(File ipDir, File cacheDir, String name) {
		super();
		this.ipDir = ipDir;
		this.cacheDir = cacheDir;
		this.name = name;
	}

	public File getIpDir() {
		return ipDir;
	}

	public File getCacheDir() {
		return cacheDir;
	}

	public String getName() {
		return name;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}
	
}
