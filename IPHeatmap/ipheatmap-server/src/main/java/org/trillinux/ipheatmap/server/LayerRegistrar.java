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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores the tile sets that are available for viewing by the user. When a
 * TileServlet comes online it should register the layer it displays with this
 * singleton. When the TileServlet is destroyed it should unregister the layer
 * that it is responsible for.
 * 
 * @author Rafael Bedia
 */
public class LayerRegistrar {
    /**
     * The single LayerRegistrar instance.
     */
    private static final LayerRegistrar INSTANCE = new LayerRegistrar();

    /**
     * The registered layers.
     */
    private final Map<String, Layer> layers;

    /**
     * Private constructor since this is a singleton.
     */
    private LayerRegistrar() {
        layers = new HashMap<String, Layer>();
    }

    /**
     * Adds a layer to the registrar.
     * 
     * @param layer
     */
    public void addLayer(Layer layer) {
        layers.put(layer.getName(), layer);
    }

    /**
     * Removes the layer with the specified name from the registrar.
     * 
     * @param name
     */
    public void removeLayer(String name) {
        layers.remove(name);
    }

    /**
     * Gets the list of registered layers.
     * 
     * @return the layers
     */
    public Collection<Layer> getLayers() {
        return layers.values();
    }

    /**
     * Gets the LayerRegistrar singleton instance.
     * 
     * @return the instance
     */
    public static LayerRegistrar getInstance() {
        return INSTANCE;
    }

}
