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
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.trillinux.ipheatmap.Tile;

/**
 * Creates tiles and sends them to the user. The user specifies the data set
 * and an x,y,z coordinate of the tile that they want. If the tile is not 
 * cached then it is generated.
 * 
 * @author Rafael Bedia
 */
public class TileServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private File ipDir;
	private File cacheDir;
	private File labelFile;
	private boolean cache = true;
	
	public TileServlet(TileServletConfig config) {
		this.ipDir = config.getIpDir();
		this.labelFile = config.getLabelFile();
		this.cacheDir = config.getCacheDir();
		this.cache = config.isCache();
	}

	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {

		ServletOutputStream output = response.getOutputStream();
		
		int z = Integer.parseInt(request.getParameter("z"));
		int x = Integer.parseInt(request.getParameter("x"));
		int y = Integer.parseInt(request.getParameter("y"));
		boolean nocache = request.getParameter("nocache") != null;

		response.setContentType("image/png");
		
		Tile tile = new Tile(ipDir, labelFile, cacheDir);
		if (cache && !nocache && tile.tileExists(x, y, z)) {
			tile.writeCachedTile(x, y, z, output);
		} else {
			tile.generate(x, y, z, output);
		}
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}
	
}
