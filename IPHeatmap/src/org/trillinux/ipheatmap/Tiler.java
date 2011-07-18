package org.trillinux.ipheatmap;

import java.awt.Point;
import java.io.File;
import java.io.IOException;


public class Tiler {

	private int maskBits;
	
	private String ipDir;
	
	private String type;
	
	public Tiler(int maskBits, String ipDir, String type) {
		this.ipDir = ipDir;
		this.type = type;
		this.maskBits = maskBits;
	}
	
	public void generate() {
		int count = 1 << maskBits;
		int lowerBits = 32 - maskBits;
		for (int i = 0; i < count; i++) {
			try {
				int subnet = i << lowerBits;
				String cidrStr = IPUtil.intToIp(subnet) + "/" + maskBits;
				System.out.println(cidrStr);
	
				CIDR cidr = CIDR.cidr_parse(cidrStr);
				IPMap h = new IPMap(cidr, 16 - maskBits);
				for (int j = 0; j < 256; j++) {
					for (int k = 0; k < 256; k++) {
						CIDR range = CIDR.cidr_parse(j + "." + k + ".0.0/8");
//						if (range.overlaps(cidr)) {
							File file = new File(ipDir, j + "/" + k + ".txt");
							if (file.exists()) {
								h.addIPMapping(new IPMapping(range, file));
							}
//						}
					}
				}
				
				h.start();
				
				Point offset = h.getOffset();
				int x = offset.x / 256;
				int y = offset.y / 256;
				int level = (maskBits / 2) + 1;
				
				h.saveImage("/home/rafael/crawler/new-" + type + "-tiles/" + level + "/" + x + "-" + y + ".png");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		long now = System.currentTimeMillis();
		
		String file = "/home/rafael/crawler/hubs/";
		Tiler tiler = new Tiler(10, file, "hub");
		tiler.generate();
		
		System.out.println((System.currentTimeMillis() - now) / 1000.0);
	}

}
