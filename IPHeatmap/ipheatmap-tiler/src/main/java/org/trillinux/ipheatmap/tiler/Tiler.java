package org.trillinux.ipheatmap.tiler;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import org.trillinux.ipheatmap.common.CIDR;
import org.trillinux.ipheatmap.common.IPListLoader;
import org.trillinux.ipheatmap.common.IPMap;
import org.trillinux.ipheatmap.common.IPUtil;


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
		if (args.length != 3) {
			System.out.println("Expected: ipDir outputDir labelFile");
		}
		
		File ipDir = new File(args[0]);
		File outputDir = new File(args[1]);
		File labelFile = new File(args[2]);
		
		Tiler tiler = new Tiler(ipDir, labelFile, outputDir);
		tiler.generate();
	}

}
