package org.trillinux.ipheatmap.tiler.test;

import org.trillinux.ipheatmap.tiler.Tiler;

public class TilerExample {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        long now = System.currentTimeMillis();

        String ipDir = "/home/rafael/crawler/smart-leaves/";
        String outputDir = "/home/rafael/crawler/smart-leaves-tiles/";
        String[] inputs = { "-i", ipDir, "-o", outputDir };
        Tiler.main(inputs);

        // String labelFile = "/home/rafael/crawler/network-labels.txt";
        // String outputDir = "/home/rafael/crawler/new-label-tiles/";
        // String[] inputs = { "-l", labelFile, "-o", outputDir };
        // Tiler.main(inputs);
        // Tiler tiler = new Tiler(new File(ipDir), new File(labelFile), new
        // File(
        // outputDir));
        // tiler.generateLevel(4);

        System.out.println((System.currentTimeMillis() - now) / 1000.0);
    }

}
