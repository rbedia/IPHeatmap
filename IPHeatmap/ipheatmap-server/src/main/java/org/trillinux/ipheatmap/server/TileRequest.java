/**
 * Copyright (C) 2014 Rafael Bedia
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

/**
 * A request for a single tile.
 *
 * @author Rafael Bedia
 */
public class TileRequest {
    public final int z;
    public final int x;
    public final int y;
    public final boolean cacheTile;
    public final String contentType;
    
    public TileRequest(String path, boolean cacheTile) throws TileRequestException {
        this.cacheTile = cacheTile;
        if (path.endsWith(".png")) {
            contentType = "image/png";
        } else {
            throw new TileRequestException("Only PNG is supported.");
        }
        path = path.substring(0, path.length() - 4);

        String[] parts = path.split("/");
        if (parts.length != 4) {
            throw new TileRequestException("Expected request in the form /z/x/y.png");
        }

        try {
            z = Integer.parseInt(parts[1]);
            x = Integer.parseInt(parts[2]);
            y = Integer.parseInt(parts[3]);
        } catch (NumberFormatException ex) {
            throw new TileRequestException("z, x, and y must be numbers.");
        }

    }
}
