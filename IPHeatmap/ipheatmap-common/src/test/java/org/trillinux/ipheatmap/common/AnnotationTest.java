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
public class AnnotationTest {

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.Annotation#getCidr()}.
     */
    @Test
    public void testGetCidr() {
        Annotation a = new Annotation(new CIDR("0.0.0.0/0"), "", "");
        assertEquals(new CIDR("0.0.0.0/0"), a.getCidr());
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.Annotation#getLabel()}.
     */
    @Test
    public void testGetLabel() {
        Annotation a = new Annotation(null, null, "");
        assertEquals("", a.getLabel());

        a = new Annotation(null, "label", "");
        assertEquals("label", a.getLabel());
    }

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.Annotation#getSublabel()}.
     */
    @Test
    public void testGetSublabel() {
        Annotation a = new Annotation(null, "", null);
        assertEquals("", a.getSublabel());

        a = new Annotation(null, "", "");
        assertEquals("", a.getSublabel());

        a = new Annotation(null, "", "prefix");
        assertEquals("", a.getSublabel());

        a = new Annotation(new CIDR("1.0.0.0/8"), "", "prefix");
        assertEquals("1.0.0.0/8", a.getSublabel());

        a = new Annotation(null, "", "asdf");
        assertEquals("asdf", a.getSublabel());

    }

}
