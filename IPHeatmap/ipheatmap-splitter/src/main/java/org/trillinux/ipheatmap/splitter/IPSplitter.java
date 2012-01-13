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
package org.trillinux.ipheatmap.splitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.trillinux.ipheatmap.common.IPUtil;

/**
 * Splits a file containing a list of IPs into multiple files based on the first
 * two levels of the IP address. For example IP address 1.2.3.4 would be written
 * to 1/1.2.0.0-16.
 * 
 * If the input file list is sorted then there is a performance benefit because
 * files do not have to be repeatedly opened and closed.
 * 
 * The directory that these files are written to is later read by the
 * IPListLoader class which creates a list of mappings from IP blocks to files.
 * It uses the file name to derive the CIDR block.
 * 
 * @author Rafael Bedia
 */
public class IPSplitter {

    private final File input;

    private final File directory;

    public IPSplitter(File input, File directory) {
        this.input = input;
        this.directory = directory;
    }

    public void start() throws IOException {
        long startTime = System.currentTimeMillis();
        directory.mkdir();

        int cutoff = 0;
        int max = 9;

        IPStorer[] storers = new IPStorer[max];
        for (int index = cutoff; index < max; index++) {
            storers[index] = new IPStorer(directory, index * 2, 16);
        }

        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null) {
            counter++;
            int ip = IPUtil.ipToInt(line);
            for (int index = cutoff; index < max; index++) {
                int hostbits = index * 2;
                int mask = (0xFFFFFFFF >> hostbits) << hostbits;
                int subnet = (ip & mask) & 0xFFFFFFFF;
                storers[index].add(subnet);
            }
            if (counter % 1000000 == 0) {
                long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                System.out.println(String.format("%12d - %12d", counter,
                        elapsed));
            }
        }

        for (int i = cutoff; i < max; i++) {
            storers[i].close();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Expected two arguments: file, directory");
            System.out
                    .println("file - The file containing a list of IP addresses one per line.");
            System.out
                    .println("directory - The directory where the split files will be saved.");
            System.exit(1);
        }

        IPSplitter splitter = new IPSplitter(new File(args[0]),
                new File(args[1]));
        splitter.start();
    }

}
