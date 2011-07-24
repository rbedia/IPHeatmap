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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.trillinux.ipheatmap.common.CIDR;
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
        directory.mkdir();

        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        String previousKey = "";
        FileOutputStream fileIndexOut = new FileOutputStream(new File(
                directory, "index.txt"));
        DataOutputStream indexOut = new DataOutputStream(fileIndexOut);
        DataOutputStream out = null;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\.");
            File level1 = new File(directory, parts[0]);
            String key = parts[0] + "." + parts[1];
            if (!previousKey.equals(key)) {
                if (out != null) {
                    out.close();
                }
                level1.mkdir();
                int mask = 16;
                String filename = String.format("%s.%s.0.0-%d", parts[0],
                        parts[1], mask);
                File ipFile = new File(level1, filename);
                FileOutputStream fileOut = new FileOutputStream(ipFile);
                out = new DataOutputStream(fileOut);

                String cidrStr = String.format("%s.%s.0.0/%d", parts[0],
                        parts[1], mask);
                CIDR cidr = new CIDR(cidrStr);
                indexOut.writeLong(cidr.getStart());
                indexOut.writeLong(cidr.getEnd());
                indexOut.writeInt(cidr.getMask());
                indexOut.writeUTF(parts[0] + '/' + filename);
            }
            out.writeLong(IPUtil.ipToInt(line));
            previousKey = key;
        }
        if (out != null) {
            out.close();
        }
        indexOut.close();
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

        IPSplitter splitter = new IPSplitter(new File(args[0]), new File(
                args[1]));
        splitter.start();
    }

}
