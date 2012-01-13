package org.trillinux.ipheatmap.splitter.test;

import org.trillinux.ipheatmap.splitter.IPSplitter;

public class SplitIPList {

    public static void main(String[] args) throws Exception {
        String file = "/home/rafael/crawler/leaves.txt";
        String directory = "/home/rafael/crawler/smart-leaves/";
        split(file, directory);
    }

    public static void split(String file, String directory) throws Exception {
        long now = System.currentTimeMillis();
        String[] inputs = { file, directory };
        IPSplitter.main(inputs);
        System.out.println((System.currentTimeMillis() - now) / 1000.0);
    }
}
