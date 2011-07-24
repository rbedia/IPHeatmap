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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for working with the network labels annotation format. Each
 * line in the file consists of three fields separated by tabs. The first field
 * is the IP block in CIDR string format. The second field is the label that
 * should be shown on the block. The third field is a sublabel which will be
 * shown in smaller text below the label. If the third field is set to the
 * special string "prefix" then the CIDR string will be shown as the sublabel.
 * 
 * @author Rafael Bedia
 */
public final class Annotate {

    /**
     * Private constructor.
     */
    private Annotate() {
    }

    /**
     * Reads a file and interprets it as a network label file. It returns a list
     * of annotations from the file.
     * 
     * @param file
     *            the network label file
     * @return a list of annotations read from the file
     * @throws IOException
     */
    public static List<Annotation> readLabelFile(final File file)
            throws IOException {
        List<Annotation> annotations = new ArrayList<Annotation>();
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            Annotation a = new Annotation(parts[0], parts[1], parts[2]);
            annotations.add(a);
        }
        return annotations;
    }
}
