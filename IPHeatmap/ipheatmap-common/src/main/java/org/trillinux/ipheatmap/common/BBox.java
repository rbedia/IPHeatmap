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
 * Defines a rectangular area bounded by the points in the lower left and upper
 * right.
 * 
 * @author Rafael Bedia
 */
public class BBox {
    private int xmin;
    private int xmax;
    private int ymin;
    private int ymax;

    public BBox() {
    }

    public BBox(BBox bbox) {
        xmin = bbox.xmin;
        xmax = bbox.xmax;
        ymin = bbox.ymin;
        ymax = bbox.ymax;
    }

    /**
     * Decreases the size of the rectangle uniformly on all sides.
     * 
     * @param xShrink
     *            the amount to remove from the left and right sides
     * @param yShrink
     *            the amount to remove from the top and bottom sides
     */
    public void shrink(int xShrink, int yShrink) {
        xmin += xShrink;
        xmax -= xShrink;
        ymin += yShrink;
        ymax -= yShrink;
    }

    /**
     * Gets the width of the rectangle.
     * 
     * @return
     */
    public int getWidth() {
        return xmax - xmin;
    }

    /**
     * Gets the height of the rectangle.
     * 
     * @return
     */
    public int getHeight() {
        return ymax - ymin;
    }

    /**
     * Moves the rectangle down and to the left by the amount specified in
     * offset. Afterwards the rectangle will have the same width and height.
     * 
     * @param offset
     */
    public void remap(Point offset) {
        xmin -= offset.x;
        xmax -= offset.x;
        ymin -= offset.y;
        ymax -= offset.y;
    }

    /**
     * Checks if the rectangle contains the point p.
     * 
     * @param p
     * @return
     */
    public boolean contains(Point p) {
        return xmin <= p.x && p.x <= xmax && ymin <= p.y && p.y <= ymax;
    }

    /**
     * Checks if box overlaps this bounding box.
     * 
     * @param box
     * @return
     */
    public boolean overlaps(BBox box) {
        boolean xOverlap = (xmin <= box.xmin && box.xmin <= xmax)
                || (box.xmin <= xmin && xmin <= box.xmax);
        boolean yOverlap = (ymin <= box.ymin && box.ymin <= ymax)
                || (box.ymin <= ymin && ymin <= box.ymax);
        return xOverlap && yOverlap;
    }

    /**
     * @return the xmin
     */
    public int getXmin() {
        return xmin;
    }

    /**
     * @param xmin
     *            the xmin to set
     */
    public void setXmin(int xmin) {
        this.xmin = xmin;
    }

    /**
     * @return the xmax
     */
    public int getXmax() {
        return xmax;
    }

    /**
     * @param xmax
     *            the xmax to set
     */
    public void setXmax(int xmax) {
        this.xmax = xmax;
    }

    /**
     * @return the ymin
     */
    public int getYmin() {
        return ymin;
    }

    /**
     * @param ymin
     *            the ymin to set
     */
    public void setYmin(int ymin) {
        this.ymin = ymin;
    }

    /**
     * @return the ymax
     */
    public int getYmax() {
        return ymax;
    }

    /**
     * @param ymax
     *            the ymax to set
     */
    public void setYmax(int ymax) {
        this.ymax = ymax;
    }
}
