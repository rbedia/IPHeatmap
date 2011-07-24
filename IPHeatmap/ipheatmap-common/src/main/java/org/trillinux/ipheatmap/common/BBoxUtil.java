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

import java.awt.Point;

/**
 * Calculates the bounding box of a CIDR mask on a hilbert curve.
 * 
 * @author Rafael Bedia
 */
public class BBoxUtil {

    private static final int MAX_MASK = 32;
    private final int hilbertCurveOrder;
    private final int bitsPerPixel;

    public BBoxUtil(final int hilbertOrder, final int inBitsPerPixel) {
        this.hilbertCurveOrder = hilbertOrder;
        this.bitsPerPixel = inBitsPerPixel;
    }

    public BBox boundingBox(final CIDR cidr) {
        BBox bbox = new BBox();
        long diag = 0xAAAAAAAAL;

        if (cidr.mask >= MAX_MASK) {
            Point p = Hilbert.getPoint(cidr.start >> 8, hilbertCurveOrder);
            bbox.xmin = p.x;
            bbox.xmax = p.x;
            bbox.ymin = p.y;
            bbox.ymax = p.y;
        } else if (0 == (cidr.mask & 1)) {
            diag >>= cidr.mask;
            Point p1 = Hilbert.getPoint(cidr.start >> bitsPerPixel,
                    hilbertCurveOrder);
            Point p2 = Hilbert.getPoint(
                    (cidr.start + diag) >> bitsPerPixel, hilbertCurveOrder);
            bbox.xmin = Math.min(p1.x, p2.x);
            bbox.xmax = Math.max(p1.x, p2.x);
            bbox.ymin = Math.min(p1.y, p2.y);
            bbox.ymax = Math.max(p1.y, p2.y);
        } else {
            CIDR cidrCopy1 = new CIDR(cidr);
            cidrCopy1.mask += 1;
            BBox b1 = boundingBox(cidrCopy1);

            CIDR cidrCopy2 = new CIDR(cidr);
            cidrCopy2.start = cidr.start + (1 << (MAX_MASK - (cidr.mask + 1)));
            cidrCopy2.mask += 1;
            BBox b2 = boundingBox(cidrCopy2);

            bbox.xmin = Math.min(b1.xmin, b2.xmin);
            bbox.xmax = Math.max(b1.xmax, b2.xmax);
            bbox.ymin = Math.min(b1.ymin, b2.ymin);
            bbox.ymax = Math.max(b1.ymax, b2.ymax);
        }

        return bbox;
    }

}
