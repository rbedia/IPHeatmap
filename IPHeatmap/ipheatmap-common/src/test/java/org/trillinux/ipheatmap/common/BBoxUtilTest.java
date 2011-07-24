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

import org.junit.Test;

/**
 * @author Rafael Bedia
 * 
 */
public class BBoxUtilTest {

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.BBoxUtil#boundingBox(org.trillinux.ipheatmap.common.CIDR)}
     * .
     */
    @Test
    public void testBoundingBox() {
        int bitsPerPixel = 28;
        int hilbertOrder = (32 - bitsPerPixel) / 2;

        BBoxUtil util = new BBoxUtil(hilbertOrder, bitsPerPixel);
        BBox bbox;

        bbox = util.boundingBox(new CIDR("0.0.0.0/2"));
        assertEquals(new BBox(0, 0, 1, 1), bbox);

        bbox = util.boundingBox(new CIDR("64.0.0.0/2"));
        assertEquals(new BBox(0, 2, 1, 3), bbox);

        bbox = util.boundingBox(new CIDR("128.0.0.0/2"));
        assertEquals(new BBox(2, 2, 3, 3), bbox);

        bbox = util.boundingBox(new CIDR("192.0.0.0/2"));
        assertEquals(new BBox(2, 0, 3, 1), bbox);

        bbox = util.boundingBox(new CIDR("0.0.0.0/1"));
        assertEquals(new BBox(0, 0, 1, 3), bbox);

        bbox = util.boundingBox(new CIDR("64.0.0.0/1"));
        assertEquals(new BBox(0, 2, 3, 3), bbox);

        bbox = util.boundingBox(new CIDR("128.0.0.0/1"));
        assertEquals(new BBox(2, 0, 3, 3), bbox);

        bbox = util.boundingBox(new CIDR("0.0.0.0/32"));
        assertEquals(new BBox(0, 0, 0, 0), bbox);

    }

}
