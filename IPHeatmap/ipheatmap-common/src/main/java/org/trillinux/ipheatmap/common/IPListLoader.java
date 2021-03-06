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
package org.trillinux.ipheatmap.common;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the mappings from CIDR blocks to files within a directory. An index
 * file is read so that the whole directory does not need to be scanned.
 * 
 * @author Rafael Bedia
 * 
 */
public final class IPListLoader {
    /**
     * Private constructor.
     */
    private IPListLoader() {
    }

    /**
     * Reads the index file in the IP directory to determine the list of IP
     * mappings that are in the directory.
     * 
     * @param ipDir
     *            the IP directory to scan
     * @return list of IP block to file mappings
     */
    public static List<IPMapping> readMappings(File ipDir) {
        List<IPMapping> mappings = new ArrayList<IPMapping>();
        try {
            FileInputStream fileIn = new FileInputStream(new File(ipDir,
                    "index.txt"));
            DataInputStream in = new DataInputStream(fileIn);

            try {
                byte type = in.readByte();

                while (true) {
                    long start = in.readLong();
                    long end = in.readLong();
                    int mask = in.readInt();
                    CIDR cidr = new CIDR(start, end, mask);
                    String filePath = in.readUTF();
                    mappings.add(new IPMapping(cidr, new File(ipDir, filePath),
                            type));
                }
            } catch (EOFException ex) {

            }
            fileIn.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return mappings;
    }
}
