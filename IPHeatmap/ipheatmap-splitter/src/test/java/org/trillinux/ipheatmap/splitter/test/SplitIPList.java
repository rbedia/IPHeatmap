package org.trillinux.ipheatmap.splitter.test;

import org.trillinux.ipheatmap.splitter.IPSplitter;

public class SplitIPList {

	public static void main(String[] args) throws Exception {
		String file = "/home/rafael/crawler/hubs.txt";
		String directory = "/home/rafael/crawler/hubs/";
		split(file, directory);
		
		file = "/home/rafael/crawler/leaves.txt";
		directory = "/home/rafael/crawler/leaves/";
		split(file, directory);
	}

	public static void split(String file, String directory) throws Exception {
		String[] inputs = {file, directory};
		IPSplitter.main(inputs);
	}
}
