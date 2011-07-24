package org.trillinux.ipheatmap.tiler.test;

import java.io.File;

import org.trillinux.ipheatmap.tiler.Tiler;

public class TilerExample {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        long now = System.currentTimeMillis();

        String ipDir = "/home/rafael/crawler/hubs/";
        String outputDir = "/home/rafael/crawler/new-hubs-tiles/";
        String labelFile = "/home/rafael/crawler/network-labels.txt";

        // String[] inputs = {ipDir, outputDir, labelFile};
        // Tiler.main(inputs);
        Tiler tiler = new Tiler(new File(ipDir), new File(labelFile), new File(
                outputDir));
        tiler.generateLevel(4);

        System.out.println((System.currentTimeMillis() - now) / 1000.0);
    }

}
