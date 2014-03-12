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

/**
 * A map layer. Each layer stores a human readable name and the pattern that is
 * used to access the tiles on the web server. For instance if a TileServlet is
 * listening at "/tiles/network" then the path would be
 * "tile/network/${z}/${x}/${y}.png".
 * 
 * This instances of this class are immutable.
 * 
 * @author Rafael Bedia
 */
public class Layer {
    /**
     * Human readable name for this layer.
     */
    private final String name;

    /**
     * The URL pattern used to access this layer of tiles.
     */
    private final String path;

    private final boolean baseLayer;

    public Layer(String name, String path, boolean baseLayer) {
        this.name = name;
        this.path = path;
        this.baseLayer = baseLayer;
    }

    /**
     * Gets the human readable name for this layer.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the URL pattern used to access this layer of tiles.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    public boolean isBaseLayer() {
        return baseLayer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Layer [name=" + name + ", path=" + path + ", baseLayer=" + baseLayer + "]";
    }

}
