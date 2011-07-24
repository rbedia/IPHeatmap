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

    private static final long DIAG_INIT = 0xAAAAAAAAL;

    private final int hilbertCurveOrder;
    private final int bitsPerPixel;

    /**
     * Constructs a BBoxUtil with the specified image dimensions derived from
     * the hilbert order and the number of bits per pixel.
     * 
     * @param hilbertOrder
     * @param inBitsPerPixel
     */
    public BBoxUtil(final int hilbertOrder, final int inBitsPerPixel) {
        this.hilbertCurveOrder = hilbertOrder;
        this.bitsPerPixel = inBitsPerPixel;
    }

    /**
     * Converts a CIDR block to a bounding box on the image.
     * 
     * @param cidr
     * @return
     */
    public BBox boundingBox(final CIDR cidr) {
        BBox bbox = new BBox();
        long diag = DIAG_INIT;

        if (cidr.getMask() >= MAX_MASK) {
            Point p = Hilbert.getPoint(cidr.getStart() >> 8, hilbertCurveOrder);
            bbox.setXmin(p.x);
            bbox.setXmax(p.x);
            bbox.setYmin(p.y);
            bbox.setYmax(p.y);
        } else if (0 == (cidr.getMask() & 1)) {
            diag >>= cidr.getMask();
            Point p1 = Hilbert.getPoint(cidr.getStart() >> bitsPerPixel,
                    hilbertCurveOrder);
            Point p2 = Hilbert
                    .getPoint((cidr.getStart() + diag) >> bitsPerPixel,
                            hilbertCurveOrder);
            bbox.setXmin(Math.min(p1.x, p2.x));
            bbox.setXmax(Math.max(p1.x, p2.x));
            bbox.setYmin(Math.min(p1.y, p2.y));
            bbox.setYmax(Math.max(p1.y, p2.y));
        } else {
            CIDR cidrCopy1 = new CIDR(cidr);
            cidrCopy1.setMask(cidrCopy1.getMask() + 1);
            BBox b1 = boundingBox(cidrCopy1);

            CIDR cidrCopy2 = new CIDR(cidr);
            cidrCopy2.setStart(cidr.getStart()
                    + (1 << (MAX_MASK - (cidr.getMask() + 1))));
            cidrCopy2.setMask(cidrCopy2.getMask() + 1);
            BBox b2 = boundingBox(cidrCopy2);

            bbox.setXmin(Math.min(b1.getXmin(), b2.getXmin()));
            bbox.setXmax(Math.max(b1.getXmax(), b2.getXmax()));
            bbox.setYmin(Math.min(b1.getYmin(), b2.getYmin()));
            bbox.setYmax(Math.max(b1.getYmax(), b2.getYmax()));
        }

        return bbox;
    }

}
