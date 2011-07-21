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

public class Annotation {
	private CIDR cidr;
	
	private String label;
	
	private String sublabel;

	public Annotation(String cidr, String label, String sublabel) {
		super();
		this.cidr = CIDR.cidr_parse(cidr);
		this.label = label;
		this.sublabel = sublabel;
	}

	public CIDR getCidr() {
		return cidr;
	}

	public void setCidr(CIDR cidr) {
		this.cidr = cidr;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSublabel() {
		return sublabel;
	}

	public void setSublabel(String sublabel) {
		this.sublabel = sublabel;
	}
	
}
