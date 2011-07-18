package org.trillinux.ipheatmap.test;

import org.trillinux.ipheatmap.Tiler;

public class TilerExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		long now = System.currentTimeMillis();
		
		String ipDir = "/home/rafael/crawler/leaves/";
		String outputDir = "/home/rafael/crawler/new-leaves-tiles/";
		String labelFile = "/home/rafael/crawler/network-labels.txt";
		
		String[] inputs = {ipDir, outputDir, labelFile};
		Tiler.main(inputs);
		
		System.out.println((System.currentTimeMillis() - now) / 1000.0);
	}

}
