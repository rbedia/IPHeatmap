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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.junit.Test;

/**
 * @author Rafael Bedia
 * 
 */
public class AnnotateTest {

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.Annotate#readLabelFile(java.io.Reader)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testReadLabelFile() throws IOException {
        URL url = AnnotateTest.class.getResource("/test-labels.txt");
        Reader reader = new InputStreamReader(url.openStream());
        List<Annotation> annotations = Annotate.readLabelFile(reader);

        Annotation a1 = annotations.get(0);
        assertEquals(new CIDR("0.0.0.0/8"), a1.getCidr());
        assertEquals("IANA - Local Identification", a1.getLabel());
        assertEquals("0.0.0.0/8", a1.getSublabel());

        Annotation a2 = annotations.get(1);
        assertEquals(new CIDR("240.0.0.0/4"), a2.getCidr());
        assertEquals("Future use", a2.getLabel());
        assertEquals("240.0.0.0/4", a2.getSublabel());
    }

}
