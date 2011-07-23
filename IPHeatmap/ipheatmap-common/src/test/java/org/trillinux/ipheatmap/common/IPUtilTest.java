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

import org.junit.Test;

/**
 * @author Rafael Bedia
 *
 */
public class IPUtilTest {

	/**
	 * Test method for {@link org.trillinux.ipheatmap.common.IPUtil#ipToInt(java.lang.String)}.
	 */
	@Test
	public void testIpToInt() {
		assertEquals(0, IPUtil.ipToInt("0.0.0.0"));
		assertEquals(255, IPUtil.ipToInt("0.0.0.255"));
		assertEquals((1 * 256) + 255, IPUtil.ipToInt("0.0.1.255"));
		assertEquals((25 * 256 * 256) + (1 * 256) + 255, IPUtil.ipToInt("0.25.1.255"));
		assertEquals((32 * 256 * 256 * 256) + (25 * 256 * 256) + (1 * 256) + 255, IPUtil.ipToInt("32.25.1.255"));
		assertEquals((255 * 256 * 256 * 256) + (255 * 256 * 256) + (255 * 256) + 255, IPUtil.ipToInt("255.255.255.255"));
		
		try {
			IPUtil.ipToInt("asdf");
			fail("Should have thrown exception.");
		} catch (IllegalArgumentException ex) {
		}
		
		try {
			IPUtil.ipToInt("a.b.c.d");
			fail("Should have thrown exception.");
		} catch (NumberFormatException ex) {
		}
	}

	/**
	 * Test method for {@link org.trillinux.ipheatmap.common.IPUtil#intToIp(int)}.
	 */
	@Test
	public void testIntToIp() {
		assertEquals("0.0.0.0", IPUtil.intToIp(0));
		assertEquals("0.0.0.255", IPUtil.intToIp(255));
		assertEquals("0.0.1.255", IPUtil.intToIp((1 * 256) + 255));
		assertEquals("0.25.1.255", IPUtil.intToIp((25 * 256 * 256) + (1 * 256) + 255));
		assertEquals("32.25.1.255", IPUtil.intToIp((32 * 256 * 256 * 256) + (25 * 256 * 256) + (1 * 256) + 255));
		assertEquals("255.255.255.255", IPUtil.intToIp((255 * 256 * 256 * 256) + (255 * 256 * 256) + (255 * 256) + 255));
	}

}
