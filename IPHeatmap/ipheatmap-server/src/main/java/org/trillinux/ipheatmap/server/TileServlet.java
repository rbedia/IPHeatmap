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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.trillinux.ipheatmap.common.Tile;

/**
 * Creates tiles and sends them to the user. The user specifies the data set and
 * an x,y,z coordinate of the tile that they want. If the tile is not cached
 * then it is generated.
 * 
 * @author Rafael Bedia
 */
public class TileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int MAX_LEVEL = 16;
    private static final int MIN_LEVEL = 0;

    private File ipDir;
    private File cacheDir;
    private File labelFile;
    private boolean enableCache = true;

    @Override
    public void init() throws ServletException {
        String ipDirPar = getServletConfig().getInitParameter("ipDir");
        if (ipDirPar != null) {
            ipDir = new File(ipDirPar);
        }
        String labelFilePar = getServletConfig().getInitParameter("labelFile");
        if (labelFilePar != null) {
            labelFile = new File(labelFilePar);
        }
        String cacheDirPar = getServletConfig().getInitParameter("cacheDir");
        if (labelFilePar != null) {
            cacheDir = new File(cacheDirPar);
        }
        String cacheStr = getServletConfig().getInitParameter("cache");
        if (cacheStr != null && cacheStr.equals("false")) {
            enableCache = false;
        }

        String layerName = getServletConfig().getInitParameter("layerName");
        String pattern = getServletConfig().getInitParameter("tilePattern");
        Layer layer = new Layer(layerName, pattern);

        if (ipDir != null) {
            if (ipDir.isDirectory()) {
                LayerRegistrar.getInstance().addLayer(layer);
            } else {
                Logger.getLogger(TileServlet.class.getName()).log(Level.WARNING, "ipDir ''{0}'' is not a directory.", ipDir);
            }
        }
    }

    @Override
    public void destroy() {
        String layerName = getServletConfig().getInitParameter("layerName");
        LayerRegistrar.getInstance().removeLayer(layerName);
    }

    /**
     * Requests come in the form of /z/x/y.png where z, x, y are positive
     * integers.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        try {
            boolean nocache = request.getParameter("nocache") != null;
            TileRequest tileRequest = new TileRequest(path, !nocache);

            response.setContentType(tileRequest.contentType);
            ServletOutputStream output = response.getOutputStream();

            createTile(tileRequest, output);
        } catch (TileRequestException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(TileServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private void createTile(TileRequest tileRequest, ServletOutputStream output)
            throws IOException {
        int z = tileRequest.z;
        int x = tileRequest.x;
        int y = tileRequest.y;
        // Find the best resolution ipDir to use for generating tiles
        File subIpDir = null;
        if (ipDir != null) {
            int ipDirLevel = MAX_LEVEL - ((z - 1) * 2);
            ipDirLevel = constraintDirLevel(ipDirLevel);
            subIpDir = new File(ipDir, "" + ipDirLevel);
        }
        Tile tile = new Tile(subIpDir, labelFile, cacheDir);
        if (enableCache && tileRequest.cacheTile && tile.tileExists(x, y, z)) {
            tile.writeCachedTile(x, y, z, output);
        } else {
            tile.generate(x, y, z, output);
        }
    }

    private int constraintDirLevel(int level) {
        level = level > MAX_LEVEL ? MAX_LEVEL : level;
        level = level < MIN_LEVEL ? MIN_LEVEL : level;
        return level;
    }
}
