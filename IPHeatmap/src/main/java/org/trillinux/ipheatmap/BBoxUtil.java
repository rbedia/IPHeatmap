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
package org.trillinux.ipheatmap;

import java.awt.Point;

/**
 * Calculates the bounding box of a CIDR mask on a hilbert curve.
 * 
 * @author Rafael Bedia
 */
public class BBoxUtil {
	
	private int hilbert_curve_order;
	private int addr_space_bits_per_pixel;

	public BBoxUtil(int hilbert_curve_order, int addr_space_bits_per_pixel) {
		this.hilbert_curve_order = hilbert_curve_order;
		this.addr_space_bits_per_pixel = addr_space_bits_per_pixel;
	}

	public BBox bounding_box(CIDR cidr) {
		BBox bbox = new BBox();
		long diag = 0xAAAAAAAAL;
		
		if (cidr.mask > 31) {
			Point p = Hilbert.hil_xy_from_s(cidr.start >> 8, hilbert_curve_order);
			bbox.xmin = p.x;
			bbox.xmax = p.x;
			bbox.ymin = p.y;
			bbox.ymax = p.y;
		} else if (0 == (cidr.mask & 1)) {
			diag >>= cidr.mask;
			Point p1 = Hilbert.hil_xy_from_s(cidr.start >> addr_space_bits_per_pixel, hilbert_curve_order);
			Point p2 = Hilbert.hil_xy_from_s((cidr.start + diag) >> addr_space_bits_per_pixel, hilbert_curve_order);
			bbox.xmin = Math.min(p1.x, p2.x);
			bbox.xmax = Math.max(p1.x, p2.x);
			bbox.ymin = Math.min(p1.y, p2.y);
			bbox.ymax = Math.max(p1.y, p2.y);
		} else {
			CIDR cidr_copy1 = new CIDR(cidr);
			cidr_copy1.mask += 1;
			BBox b1 = bounding_box(cidr_copy1);

			CIDR cidr_copy2 = new CIDR(cidr);
			cidr_copy2.start = cidr.start + (1 << (32 - (cidr.mask + 1)));
			cidr_copy2.mask += 1;
			BBox b2 = bounding_box(cidr_copy2);
			
			bbox.xmin = Math.min(b1.xmin, b2.xmin);
			bbox.xmax = Math.max(b1.xmax, b2.xmax);
			bbox.ymin = Math.min(b1.ymin, b2.ymin);
			bbox.ymax = Math.max(b1.ymax, b2.ymax);
		}
		
		return bbox;
	}
	
}
