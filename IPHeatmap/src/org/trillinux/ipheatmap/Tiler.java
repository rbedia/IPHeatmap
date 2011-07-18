package org.trillinux.ipheatmap;

import java.awt.Point;
import java.io.File;
import java.io.IOException;


public class Tiler {

	private File ipDir;
	
	private File labelFile;
	
	private File outputDir;
	
	public Tiler(File ipDir, File labelFile, File outputDir) {
		this.ipDir = ipDir;
		this.labelFile = labelFile;
		this.outputDir = outputDir;
	}
	
	/**
	 * Generates all of the tiles at a particular zoom level.
	 * 
	 * TODO rbedia: convert this from using maskBits to using zoom level
	 * 
	 * @param maskBits
	 */
	public void generateLevel(int maskBits) {
		int count = 1 << maskBits;
		int lowerBits = 32 - maskBits;
		for (int i = 0; i < count; i++) {
			try {
				int subnet = i << lowerBits;
				String cidrStr = IPUtil.intToIp(subnet) + "/" + maskBits;
				System.out.println(cidrStr);
	
				CIDR cidr = CIDR.cidr_parse(cidrStr);
				IPMap h = new IPMap(cidr, 16 - maskBits);
				
				IPListLoader loader = new IPListLoader(ipDir);
				h.addIPMappings(loader.getMappings());
				h.setLabelFile(labelFile);
				h.start();
				
				Point offset = h.getOffset();
				int x = offset.x / 256;
				int y = offset.y / 256;
				int level = (maskBits / 2) + 1;
				
				File tile = new File(outputDir, level + "/" + x + "/" + y + ".png");
				File parent = tile.getParentFile();
				parent.mkdirs();
				if (parent.exists()) {
					h.saveImage(tile);
				} else {
					System.out.println("Directory couldn't be created: " + parent);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Generates the tiles for all zoom levels.
	 */
	public void generate() {
		for (int i = 0; i <= 16; i += 2) {
			generateLevel(i);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		long now = System.currentTimeMillis();
		
		File ipDir = new File("/home/rafael/crawler/hubs/");
		File outputDir = new File("/home/rafael/crawler/new-hubs-tiles/");
		File labelFile = new File("/home/rafael/crawler/network-labels.txt");
		
		for (int i = 0; i <= 16; i += 2) {
			Tiler tiler = new Tiler(ipDir, labelFile, outputDir);
			tiler.generateLevel(i);
		}
		
		System.out.println((System.currentTimeMillis() - now) / 1000.0);
	}

}
