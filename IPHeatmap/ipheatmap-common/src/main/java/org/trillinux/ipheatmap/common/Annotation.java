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
 * The label is usually the name of the owner of the network block but it can
 * be anything. The sublabel is additional information about the network block.
 * If the sublabel is set to "prefix" then it will show the text form of the
 * CIDR block that the annotation represents. 
 * 
 * @author Rafael Bedia
 */
public class Annotation {
	private CIDR cidr;
	
	private String label;
	
	private String sublabel;

	public Annotation(String cidr, String label, String sublabel) {
		this(new CIDR(cidr), label, sublabel);
	}

	public Annotation(CIDR cidr, String label, String sublabel) {
		this.cidr = cidr;
		this.label = label;
		if (sublabel == null) {
			this.sublabel = "";
		} else {
			this.sublabel = sublabel;
		}
	}

	public CIDR getCidr() {
		return cidr;
	}

	public String getLabel() {
		return label;
	}

	public String getSublabel() {
		if (sublabel.equals("prefix")) {
			return cidr.getText();
		} else {
			return sublabel;
		}
	}
}
