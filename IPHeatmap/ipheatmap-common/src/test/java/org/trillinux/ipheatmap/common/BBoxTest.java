/**
 * 
 */
package org.trillinux.ipheatmap.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

/**
 * @author Rafael Bedia
 * 
 */
public class BBoxTest {

    /**
     * Test method for {@link org.trillinux.ipheatmap.common.BBox#BBox()}.
     */
    @Test
    public void testBBox() {
        BBox bbox = new BBox();
        assertEquals(0, bbox.getXmin());
        assertEquals(0, bbox.getXmax());
        assertEquals(0, bbox.getYmin());
        assertEquals(0, bbox.getYmax());
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.BBox#BBox(org.trillinux.ipheatmap.common.BBox)}
     * .
     */
    @Test
    public void testBBoxBBox() {
        BBox exp = new BBox(1, 2, 3, 4);
        BBox res = new BBox(exp);

        assertEquals(exp.getXmin(), res.getXmin());
        assertEquals(exp.getXmax(), res.getXmax());
        assertEquals(exp.getYmin(), res.getYmin());
        assertEquals(exp.getYmax(), res.getYmax());
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.BBox#shrink(int, int)}.
     */
    @Test
    public void testShrink() {
        BBox exp = new BBox(12, 10, 18, 20);
        BBox res = new BBox(10, 5, 20, 25);
        res.shrink(2, 5);
        assertEquals(exp, res);
    }

    /**
     * Test method for {@link org.trillinux.ipheatmap.common.BBox#getWidth()}.
     */
    @Test
    public void testGetWidth() {
        BBox bbox = new BBox(10, 5, 20, 25);
        assertEquals(10, bbox.getWidth());
    }

    /**
     * Test method for {@link org.trillinux.ipheatmap.common.BBox#getHeight()}.
     */
    @Test
    public void testGetHeight() {
        BBox bbox = new BBox(10, 5, 20, 25);
        assertEquals(20, bbox.getHeight());
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.BBox#remap(java.awt.Point)}.
     */
    @Test
    public void testRemap() {
        BBox exp = new BBox(0, 0, 10, 20);
        BBox bbox = new BBox(10, 5, 20, 25);
        bbox.remap(new Point(10, 5));
        assertEquals(exp, bbox);
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.BBox#contains(java.awt.Point)}.
     */
    @Test
    public void testContains() {
        BBox bbox = new BBox(10, 5, 20, 25);
        assertTrue(bbox.contains(new Point(10, 5)));
        assertTrue(bbox.contains(new Point(20, 25)));
        assertTrue(bbox.contains(new Point(15, 10)));

        assertFalse(bbox.contains(new Point(9, 5)));
        assertFalse(bbox.contains(new Point(10, 4)));
        assertFalse(bbox.contains(new Point(9, 4)));

        assertFalse(bbox.contains(new Point(21, 25)));
        assertFalse(bbox.contains(new Point(20, 26)));
        assertFalse(bbox.contains(new Point(21, 26)));
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.BBox#overlaps(org.trillinux.ipheatmap.common.BBox)}
     * .
     */
    @Test
    public void testOverlaps() {
        BBox bbox1 = new BBox(10, 5, 20, 25);
        BBox bbox2;

        // Test two identical bounding boxes
        bbox2 = new BBox(10, 5, 20, 25);
        assertTrue(bbox1.overlaps(bbox2));

        // Test bounding boxes that touch on the edges
        bbox2 = new BBox(0, 5, 10, 25);
        assertTrue(bbox1.overlaps(bbox2));

        bbox2 = new BBox(10, 0, 20, 5);
        assertTrue(bbox1.overlaps(bbox2));

        bbox2 = new BBox(20, 5, 30, 25);
        assertTrue(bbox1.overlaps(bbox2));

        bbox2 = new BBox(10, 25, 20, 35);
        assertTrue(bbox1.overlaps(bbox2));

        // Test bounding boxes that touch on the corners
        bbox2 = new BBox(0, 0, 10, 5);
        assertTrue(bbox1.overlaps(bbox2));

        bbox2 = new BBox(20, 25, 25, 35);
        assertTrue(bbox1.overlaps(bbox2));

        bbox2 = new BBox(0, 25, 10, 35);
        assertTrue(bbox1.overlaps(bbox2));

        bbox2 = new BBox(20, 0, 30, 5);
        assertTrue(bbox1.overlaps(bbox2));

        // Test bounding box completely inside
        bbox2 = new BBox(12, 7, 18, 23);
        assertTrue(bbox1.overlaps(bbox2));

        // Test bounding box completely surrounding
        bbox2 = new BBox(0, 0, 30, 35);
        assertTrue(bbox1.overlaps(bbox2));

        // Test bounding boxes that do not overlap
        bbox2 = new BBox(0, 0, 1, 1);
        assertFalse(bbox1.overlaps(bbox2));

        bbox2 = new BBox(12, 0, 18, 3);
        assertFalse(bbox1.overlaps(bbox2));

        bbox2 = new BBox(22, 0, 25, 3);
        assertFalse(bbox1.overlaps(bbox2));

        bbox2 = new BBox(22, 6, 25, 10);
        assertFalse(bbox1.overlaps(bbox2));

        bbox2 = new BBox(22, 26, 25, 30);
        assertFalse(bbox1.overlaps(bbox2));

        bbox2 = new BBox(12, 26, 18, 30);
        assertFalse(bbox1.overlaps(bbox2));

        bbox2 = new BBox(0, 26, 1, 30);
        assertFalse(bbox1.overlaps(bbox2));

        bbox2 = new BBox(0, 12, 1, 22);
        assertFalse(bbox1.overlaps(bbox2));

    }

}
