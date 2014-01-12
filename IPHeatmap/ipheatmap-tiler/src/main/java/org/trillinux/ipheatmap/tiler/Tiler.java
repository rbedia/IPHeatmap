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
package org.trillinux.ipheatmap.tiler;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.trillinux.ipheatmap.common.CIDR;
import org.trillinux.ipheatmap.common.IPListLoader;
import org.trillinux.ipheatmap.common.IPMap;
import org.trillinux.ipheatmap.common.IPUtil;

/**
 * Creates all of the tiles needed for a static deploy of the IPHeatmap.
 * 
 * @author Rafael Bedia
 */
public class Tiler {
    private static final int THREADS = 8;
    
    private ExecutorService executor;
    
    private final File ipDir;

    private final File labelFile;

    private final File outputDir;

    /**
     * Initializes the Tiler with the input and output locations.
     * 
     * @param ipDir
     *            the directory that stores IP lists. Use IPSplitter to
     *            generate.
     * @param labelFile
     *            IP block label file
     * @param outputDir
     *            directory to store the generated tiles
     */
    public Tiler(File ipDir, File labelFile, File outputDir) {
        this.ipDir = ipDir;
        this.labelFile = labelFile;
        this.outputDir = outputDir;
        
        this.executor = Executors.newFixedThreadPool(THREADS);
    }

    /**
     * Generates the tiles for all zoom levels.
     */
    public void generate() throws InterruptedException {
        for (int i = 0; i <= 16; i += 2) {
            generateLevel(i);
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    /**
     * Generates all of the tiles at a particular zoom level.
     * 
     * TODO rbedia: convert this from using maskBits to using zoom level
     * 
     * @param maskBits
     */
    public void generateLevel(int maskBits) {
        int count = 1 << maskBits;
        int lowerBits = 32 - maskBits;
        for (int i = 0; i < count; i++) {
            executor.execute(new TileRunnable(i, lowerBits, maskBits));
        }
    }
    
    private class TileRunnable implements Runnable {

        private int i;
        private int lowerBits;
        private int maskBits;

        public TileRunnable(int i, int lowerBits, int maskBits) {
            this.i = i;
            this.lowerBits = lowerBits;
            this.maskBits = maskBits;
        }
        
        @Override
        public void run() {
            createTile(i, lowerBits, maskBits);
        }

        private void createTile(int i, int lowerBits, int maskBits) {
            try {
                int subnet = i << lowerBits;
                String cidrStr = IPUtil.intToIp(subnet) + "/" + maskBits;
                System.out.println(cidrStr);

                CIDR cidr = new CIDR(cidrStr);
                IPMap h = new IPMap(cidr, 16 - maskBits);

                if (ipDir != null) {
                    File dir = new File(ipDir, Integer.toString(16 - maskBits));
                    h.addIPMappings(IPListLoader.readMappings(dir));
                    h.setLabelFile(labelFile);
                    h.startIPMap();
                } else {
                    h.setLabelFile(labelFile);
                    h.startLabelMap();
                }

                Point offset = h.getOffset();
                int x = offset.x / 256;
                int y = offset.y / 256;
                int level = (maskBits / 2) + 1;

                File tile = new File(outputDir, level + "/" + x + "/" + y
                        + ".png");
                File parent = tile.getParentFile();
                parent.mkdirs();
                if (parent.exists()) {
                    h.saveImage(tile);
                } else {
                    System.out.println("Directory couldn't be created: "
                            + parent);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser("i:l:o:");
        OptionSet options = parser.parse(args);

        if (!options.hasArgument("o")) {
            System.out.println("Output directory required. Use -o");
            System.exit(1);
        }
        if (!options.hasArgument("i") && !options.hasArgument("l")) {
            System.out.println("Must specify -i ipDir or -l labelFile");
            System.exit(1);
        }

        File outputDir = new File((String) options.valueOf("o"));
        File ipDir = null;
        File labelFile = null;

        if (options.hasArgument("i")) {
            ipDir = new File((String) options.valueOf("i"));
        }

        if (options.hasArgument("l")) {
            labelFile = new File((String) options.valueOf("l"));
        }

        Tiler tiler = new Tiler(ipDir, labelFile, outputDir);
        tiler.generate();
    }

}
