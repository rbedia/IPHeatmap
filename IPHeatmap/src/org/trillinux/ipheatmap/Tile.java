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

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Creates a single tile. Can also read a tile from cache if it exists.
 * 
 * @author Rafael Bedia
 */
public class Tile {

	private File ipDir;
	private File cacheDir;
	
	/**
	 * 
	 * @param ipDir The directory containing the IP lists
	 * @param cacheDir The directory where generated tiles will be cached
	 */
	public Tile(File ipDir, File cacheDir) {
		this.ipDir = ipDir;
		this.cacheDir = cacheDir;
	}

	/**
	 * Creates a tile at the given coordinates. The tile will be cached to disk
	 * and written to the OutputStream.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Zoom level
	 * @param out where to write the tile image
	 */
	public void generate(int x, int y, int z, OutputStream out) {
		try {
			Point offset = new Point(x * 256, y * 256);
			int maskBits = (z - 1) * 2;
			BBox box = new BBox();
			box.xmin = offset.x;
			box.ymin = offset.y;
			box.xmax = box.xmin + 256;
			box.ymax = box.ymin + 256;

			IPMap h = new IPMap(box, maskBits, 16 - maskBits);
			for (int j = 0; j < 256; j++) {
				for (int k = 0; k < 256; k++) {
					CIDR range = CIDR.cidr_parse(j + "." + k + ".0.0/16");
					File file = new File(ipDir, j + "/" + k + ".txt");
					if (file.exists()) {
						h.addIPMapping(new IPMapping(range, file));
					}
				}
			}
			h.start();

			h.saveImage(cacheDir + "/" + z + "/" + x + "-" + y + ".png");

			if (out != null) {
				h.writeImage(out);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Writes a tile to the OutputStream from the disk cache. This method 
	 * should only be called if tileExists returns true for the same (x, y, z)
	 * triplet.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Zoom level
	 * @param out where to write the tile image
	 * @throws IOException
	 */
	public void writeCachedTile(int x, int y, int z, OutputStream out)
			throws IOException {
		FileInputStream is = new FileInputStream(new File(cacheDir, z + "/" + x
				+ "-" + y + ".png"));

		byte[] buffer = new byte[4096];
		int len;
		while ((len = is.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
		is.close();
		out.close();
	}

	/**
	 * Checks the tile cache to see if the tile has already been created.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Zoom level
	 * @return true if the tile exists, false otherwise
	 */
	public boolean tileExists(int x, int y, int z) {
		File tilefile = new File(cacheDir, z + "/" + x + "-" + y
				+ ".png");
		return tilefile.exists();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		long now = System.currentTimeMillis();

		String type = "hubs";
		File cacheDir = new File("/home/rafael/crawler/tile-cache/" + type);
		File ipDir = new File("/home/rafael/crawler/" + type);
		Tile tile = new Tile(ipDir, cacheDir);
		tile.generate(0, 0, 9, null);

		System.out.println((System.currentTimeMillis() - now) / 1000.0);
	}

}
