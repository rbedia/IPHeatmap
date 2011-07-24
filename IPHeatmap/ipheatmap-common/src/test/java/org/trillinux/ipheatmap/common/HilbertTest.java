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

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

/**
 * @author Rafael Bedia
 *
 */
public class HilbertTest {

	/**
	 * Test for order 1
	 */
	@Test
	public void testOrder1() {
		Point res;
		
		res = Hilbert.getPoint(0, 1);
		assertEquals(new Point(0, 0), res);

		res = Hilbert.getPoint(1, 1);
		assertEquals(new Point(0, 1), res);

		res = Hilbert.getPoint(2, 1);
		assertEquals(new Point(1, 1), res);

		res = Hilbert.getPoint(3, 1);
		assertEquals(new Point(1, 0), res);
		
	}

	/**
	 * Test for order 2
	 */
	@Test
	public void testOrder2() {
		Point res;

		res = Hilbert.getPoint(0, 2);
		assertEquals(new Point(0, 0), res);

		res = Hilbert.getPoint(1, 2);
		assertEquals(new Point(1, 0), res);

		res = Hilbert.getPoint(2, 2);
		assertEquals(new Point(1, 1), res);

		res = Hilbert.getPoint(3, 2);
		assertEquals(new Point(0, 1), res);

		res = Hilbert.getPoint(4, 2);
		assertEquals(new Point(0, 2), res);

		res = Hilbert.getPoint(5, 2);
		assertEquals(new Point(0, 3), res);

		res = Hilbert.getPoint(6, 2);
		assertEquals(new Point(1, 3), res);

		res = Hilbert.getPoint(7, 2);
		assertEquals(new Point(1, 2), res);

		res = Hilbert.getPoint(8, 2);
		assertEquals(new Point(2, 2), res);

		res = Hilbert.getPoint(9, 2);
		assertEquals(new Point(2, 3), res);

		res = Hilbert.getPoint(10, 2);
		assertEquals(new Point(3, 3), res);

		res = Hilbert.getPoint(11, 2);
		assertEquals(new Point(3, 2), res);

		res = Hilbert.getPoint(12, 2);
		assertEquals(new Point(3, 1), res);

		res = Hilbert.getPoint(13, 2);
		assertEquals(new Point(2, 1), res);

		res = Hilbert.getPoint(14, 2);
		assertEquals(new Point(2, 0), res);
		
		res = Hilbert.getPoint(15, 2);
		assertEquals(new Point(3, 0), res);
		
	}

	/**
	 * Odd Hilbert orders are the transpose of even Hilbert orders
	 */
	@Test
	public void testEvenOdd() {
		Point res;
		res = Hilbert.getPoint(16, 3);
		assertEquals(new Point(0, 4), res);
		
		res = Hilbert.getPoint(16, 4);
		assertEquals(new Point(4, 0), res);
		
		res = Hilbert.getPoint(16, 5);
		assertEquals(new Point(0, 4), res);
		
		res = Hilbert.getPoint(16, 6);
		assertEquals(new Point(4, 0), res);
	}
}
