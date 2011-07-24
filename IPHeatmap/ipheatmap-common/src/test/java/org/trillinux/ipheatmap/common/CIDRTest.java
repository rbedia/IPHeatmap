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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Rafael Bedia
 * 
 */
public class CIDRTest {

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.CIDR#CIDR(org.trillinux.ipheatmap.common.CIDR)}
     * .
     */
    @Test
    public void testCIDRCIDR() {
        CIDR cidr = new CIDR("0.0.1.0/24");
        CIDR copy = new CIDR(cidr);
        assertEquals(cidr.getStart(), copy.getStart());
        assertEquals(cidr.getEnd(), copy.getEnd());
        assertEquals(cidr.getMask(), copy.getMask());
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.CIDR#CIDR(java.lang.String)}.
     */
    @Test
    public void testCIDRString() {
        CIDR res, exp;

        // Test the low end
        exp = new CIDR(0, (1L << 32) - 1, 0);
        res = new CIDR("0.0.0.0/0");
        assertEquals(exp, res);

        // Test the high end
        exp = new CIDR((1L << 32) - 1, (1L << 32) - 1, 32);
        res = new CIDR("255.255.255.255/32");
        assertEquals(exp, res);

        // Test in the middle
        exp = new CIDR((1L << 24), (2L << 24) - 1, 8);
        res = new CIDR("1.0.0.0/8");
        assertEquals(exp, res);
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.CIDR#overlaps(org.trillinux.ipheatmap.common.CIDR)}
     * .
     */
    @Test
    public void testOverlaps() {
        CIDR cidr1;
        CIDR cidr2;

        cidr1 = new CIDR("0.0.0.0/0");
        cidr2 = new CIDR("1.2.3.4/32");
        assertTrue(cidr1.overlaps(cidr2));
        assertTrue(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("1.2.3.4/32");
        cidr2 = new CIDR("1.2.3.4/32");
        assertTrue(cidr1.overlaps(cidr2));
        assertTrue(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("1.2.3.4/32");
        cidr2 = new CIDR("0.0.0.0/0");
        assertTrue(cidr1.overlaps(cidr2));
        assertTrue(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("255.255.255.255/32");
        cidr2 = new CIDR("0.0.0.0/0");
        assertTrue(cidr1.overlaps(cidr2));
        assertTrue(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("1.0.0.0/32");
        cidr2 = new CIDR("1.0.0.0/8");
        assertTrue(cidr1.overlaps(cidr2));
        assertTrue(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("1.255.255.255/32");
        cidr2 = new CIDR("1.0.0.0/8");
        assertTrue(cidr1.overlaps(cidr2));
        assertTrue(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("0.255.255.255/32");
        cidr2 = new CIDR("1.0.0.0/8");
        assertFalse(cidr1.overlaps(cidr2));
        assertFalse(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("2.0.0.0/32");
        cidr2 = new CIDR("1.0.0.0/8");
        assertFalse(cidr1.overlaps(cidr2));
        assertFalse(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("1.1.0.0/16");
        cidr2 = new CIDR("1.0.0.0/8");
        assertTrue(cidr1.overlaps(cidr2));
        assertTrue(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("1.1.24.0/24");
        cidr2 = new CIDR("1.0.0.0/8");
        assertTrue(cidr1.overlaps(cidr2));
        assertTrue(cidr2.overlaps(cidr1));

        cidr1 = new CIDR("3.0.0.0/24");
        cidr2 = new CIDR("1.0.0.0/8");
        assertFalse(cidr1.overlaps(cidr2));
        assertFalse(cidr2.overlaps(cidr1));
    }

}
