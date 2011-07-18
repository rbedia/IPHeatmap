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
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Creates a Jetty server for serving the tiles for an IP Map.
 * 
 * @author Rafael Bedia
 */
public class TileServer {

	private TileServletConfig[] configs;
	
	public TileServer(TileServletConfig[] configs) {
		this.configs = configs;
	}

	public void go() throws Exception {
		Server server = new Server(8080);

		ContextHandlerCollection rootHandler = new ContextHandlerCollection();

		// Create a handler for generating tiles
		ServletContextHandler tileHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		tileHandler.setContextPath("/tile");
		
		for (TileServletConfig config : configs) {
			tileHandler.addServlet(new ServletHolder(new TileServlet(config)), "/" + config.getName() + "/*");
		}

		rootHandler.addHandler(tileHandler);
		
		final String webAppDir = "org/trillinux/ipheatmap/server/webapp";
		final String jspPath = "/";

		// JSP directory
		final URL warUrl = TileServer.class.getClassLoader().getResource(
				webAppDir);
		final String warUrlString = warUrl.toExternalForm();

		// Set the JSP pages to be rooted at /
		rootHandler.addHandler(new WebAppContext(warUrlString, jspPath));

		server.setHandler(rootHandler);

		server.start();
		server.join();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TileServletConfig[] configs = new TileServletConfig[2];

		configs[0] = new TileServletConfig(
			new File("/home/rafael/crawler/hubs"),
			new File("/home/rafael/crawler/network-labels.txt"),
			new File("/home/rafael/crawler/tile-cache/hubs"),
			"hubs");

		configs[1] = new TileServletConfig(
			new File("/home/rafael/crawler/leaves"),
			new File("/home/rafael/crawler/network-labels.txt"),
			new File("/home/rafael/crawler/tile-cache/leaves"),
			"leaves");
		
		TileServer server = new TileServer(configs);
		try {
			server.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
