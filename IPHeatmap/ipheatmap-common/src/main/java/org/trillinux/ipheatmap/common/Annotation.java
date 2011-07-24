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

/**
 * A network block annotation. An annotation is attached to a network block and
 * shows a label and a sublabel. The network block is specified in CIDR format.
 * The label is usually the name of the owner of the network block but it can be
 * anything. The sublabel is additional information about the network block. If
 * the sublabel is set to "prefix" then it will show the text form of the CIDR
 * block that the annotation represents.
 * 
 * Instances of this class are immutable.
 * 
 * @author Rafael Bedia
 */
public class Annotation {
    private final CIDR cidr;

    private final String label;

    private String sublabel;

    /**
     * Constructs an Annotation.
     * 
     * @param cidr
     * @param label
     * @param sublabel
     */
    public Annotation(CIDR cidr, String label, String sublabel) {
        this.cidr = cidr;
        if (label == null) {
            this.label = "";
        } else {
            this.label = label;
        }
        if (sublabel == null) {
            this.sublabel = "";
        } else {
            this.sublabel = sublabel;
        }
    }

    /**
     * Gets the CIDR block.
     * 
     * @return the CIDR block
     */
    public CIDR getCidr() {
        return cidr;
    }

    /**
     * Gets the label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the sublabel.
     * 
     * @return the sublabel
     */
    public String getSublabel() {
        if (sublabel.equals("prefix")) {
            if (cidr != null) {
                return cidr.getText();
            } else {
                return "";
            }
        } else {
            return sublabel;
        }
    }
}
