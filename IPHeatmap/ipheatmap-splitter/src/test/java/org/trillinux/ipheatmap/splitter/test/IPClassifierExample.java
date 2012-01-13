package org.trillinux.ipheatmap.splitter.test;

import org.trillinux.ipheatmap.common.IPUtil;

public class IPClassifierExample {

    public IPClassifierExample() {

    }

    public void classify(String input, int bucketbits, int tilebits) {
        long ip = IPUtil.ipToLong(input) & 0xFFFFFFFF;
        System.out.println("ip: " + input + " " + ip);
        long mask = (0xFFFFFFFF >> bucketbits) << bucketbits;
        long subnet = (ip & mask) & 0xFFFFFFFF;
        long bucket = subnet >> (bucketbits + tilebits) << (bucketbits + tilebits);
        String subnetStr = IPUtil.intToIp((int) subnet);
        String bucketStr = Long.toHexString(bucket);
        System.out.println("subnet: " + subnetStr + " " + subnet);
        System.out.println("bucket: " + bucketStr + " " + bucket);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        IPClassifierExample classifier = new IPClassifierExample();
        final int tilebits = 16;
        classifier.classify("251.1.1.1", 0, tilebits);
        classifier.classify("251.2.1.1", 0, tilebits);
        classifier.classify("251.1.1.1", 16, tilebits);
        classifier.classify("251.2.1.1", 16, tilebits);
    }

}
