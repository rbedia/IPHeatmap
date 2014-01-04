package org.trillinux.ipheatmap.splitter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.trillinux.ipheatmap.common.CIDR;

public class IPStorer {

    private final File directory;

    private final int bucketbits;
    private int bucketMask;

    private final int hostbits;
    private int curSubnet;
    private int count;

    DataOutputStream stream;

    DataOutputStream indexOut;

    private int curBucketSubnet;

    /**
     * @param baseDirectory
     * @throws IOException
     */
    public IPStorer(File baseDirectory, int hostbits, int bucketbits)
            throws IOException {
        this.directory = new File(baseDirectory, Integer.toString(hostbits));
        this.hostbits = hostbits;
        this.bucketbits = bucketbits;

        this.directory.mkdir();

        curSubnet = -1;
        curBucketSubnet = -1;
        count = 0;
        bucketMask = (0xFFFFFFFF >> (hostbits + bucketbits)) << (hostbits + bucketbits);
        if (bucketMask == 0xFFFFFFFF) {
            bucketMask = 0;
        }

        FileOutputStream fileIndexOut = new FileOutputStream(new File(
                this.directory, "index.txt"));
        indexOut = new DataOutputStream(fileIndexOut);
        indexOut.writeByte(1); // the index type, 1 for tiered
    }

    public synchronized void add(int subnet) throws IOException {
        int newBucketSubnet = (subnet & bucketMask) & 0xFFFFFFFF;
        if (newBucketSubnet != curBucketSubnet) {
            if (stream != null) {
                stream.close();
            }

            curBucketSubnet = newBucketSubnet;
            stream = new DataOutputStream(new FileOutputStream(new File(
                    directory, Integer.toHexString(curBucketSubnet) + ".txt")));

            int end = (int) (curBucketSubnet & CIDR.MAX_IP | (CIDR.MAX_IP >> bucketbits));

            indexOut.writeLong(curBucketSubnet);
            indexOut.writeLong(end);
            indexOut.writeInt(bucketbits);
            indexOut.writeUTF(Integer.toHexString(curBucketSubnet) + ".txt");
        }

        if (curSubnet == subnet) {
            count++;
        } else {
            if (count > 0) {
                stream.writeInt(curSubnet);
                stream.writeByte(hostbits);
                stream.writeInt(count);
            }
            curSubnet = subnet;
            count = 1;
        }
    }

    public void close() throws IOException {
        indexOut.close();
        stream.close();
    }
}
