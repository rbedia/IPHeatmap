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

public class BBox {
	public int xmin;
	public int xmax;
	public int ymin;
	public int ymax;
	
	public BBox() {}
	
	public BBox(BBox bbox) {
		xmin = bbox.xmin;
		xmax = bbox.xmax;
		ymin = bbox.ymin;
		ymax = bbox.ymax;
	}
	
	public void shrink(int xShrink, int yShrink) {
		xmin += xShrink;
		xmax -= xShrink;
		ymin += yShrink;
		ymax -= yShrink;
	}
	
	public int getWidth() {
		return xmax - xmin;
	}
	
	public int getHeight() {
		return ymax - ymin;
	}

	public void remap(Point offset) {
		xmin -= offset.x;
		xmax -= offset.x;
		ymin -= offset.y;
		ymax -= offset.y;
	}
	
	public boolean contains(Point p) {
		return xmin <= p.x && xmax >= p.x && ymin <= p.y && ymax >= p.y;
	}
	
	public boolean overlaps(BBox box) {
		boolean xOverlap = (xmin <= box.xmin && box.xmin <= xmax) || (box.xmin <= xmin && xmin <= box.xmax);
		boolean yOverlap = (ymin <= box.ymin && box.ymin <= ymax) || (box.ymin <= ymin && ymin <= box.ymax);
		return xOverlap && yOverlap;
	}
}
