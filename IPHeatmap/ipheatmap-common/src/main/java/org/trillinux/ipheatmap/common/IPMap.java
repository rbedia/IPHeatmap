/**
 * Copyright (C) 2011 Rafael Bedia
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * http://www.gnu.org/copyleft/gpl.html
 */
package org.trillinux.ipheatmap.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class IPMap {

    private static final int ADDR_SPACE_BITS = 32;

    private static final int NUM_DATA_COLORS = 256;

    private int bitsPerPixel;

    private final int hilbertOrder;

    private int imageWidth;

    private int imageHeight;

    private BufferedImage bimage;

    private final BBoxUtil bboxUtil;

    private Color[] colors;

    private BBox subset;
    private Point offset;

    private List<IPMapping> iplists;

    private int[][] ipCounts;

    private int maxCount = 0;

    private File labelFile = new File("network-labels.txt");

    /**
     * Creates an IP heatmap.
     * 
     * cidr defines the subset of the IP space that will be mapped.
     * 
     * bitsPerPixel determines how many IP addresses will be represented by each
     * pixel.
     * 
     * The combination of cidr and bitsPerPixel determines the size of the
     * resulting image.
     */
    public IPMap(CIDR cidr, int bitsPerPixel) {
        hilbertOrder = (ADDR_SPACE_BITS - bitsPerPixel) / 2;
        bboxUtil = new BBoxUtil(hilbertOrder, bitsPerPixel);
        init(bboxUtil.boundingBox(cidr), cidr.getMask(), bitsPerPixel);
    }

    public IPMap(BBox subset, int mask, int bitsPerPixel) {
        hilbertOrder = (ADDR_SPACE_BITS - bitsPerPixel) / 2;
        bboxUtil = new BBoxUtil(hilbertOrder, bitsPerPixel);
        init(subset, mask, bitsPerPixel);
    }

    private void init(BBox subset, int mask, int bitsPerPixel) {
        this.bitsPerPixel = bitsPerPixel;
        iplists = new ArrayList<IPMapping>();
        int bitsPerImage = ADDR_SPACE_BITS - mask;
        this.subset = subset;
        offset = new Point(subset.getXmin(), subset.getYmin());
        int size = (bitsPerImage - bitsPerPixel) / 2;
        imageWidth = 1 << size;
        imageHeight = 1 << size;
        ipCounts = new int[imageWidth][imageHeight];
        loadColors();
    }

    /**
     * Adds an IPMapping which is a map from a CIDR mask to a file that contains
     * a list of IPs that fall within that range. The IP addresses in the file
     * will be mapped if they fall within the range being mapped. This is an
     * optimization so that not as many IPs have to be checked for inclusion in
     * the final image.
     * 
     * @param mapping
     */
    public void addIPMapping(IPMapping mapping) {
        iplists.add(mapping);
    }

    /**
     * Adds a list of IPMappings which are maps from a CIDR mask to a file that
     * contains a list of IPs that fall within that range. The IP addresses in
     * the file will be mapped if they fall within the range being mapped. This
     * is an optimization so that not as many IPs have to be checked for
     * inclusion in the final image.
     * 
     * @param mappings
     */
    public void addIPMappings(List<IPMapping> mappings) {
        iplists.addAll(mappings);
    }

    /**
     * Creates the colors that will be used on the heatmap. The range is from
     * blue to red.
     */
    private void loadColors() {
        colors = new Color[NUM_DATA_COLORS];
        for (int i = 0; i < colors.length; i++) {
            float hue = (240.0f * (255 - i) / 255) / 360.0f;
            int c = Color.HSBtoRGB(hue, 1.0f, 1.0f);
            colors[i] = new Color(c);
        }
    }

    public void saveImage(File file) throws IOException {
        ImageIO.write(bimage, "png", file);
    }

    public void writeImage(OutputStream out) throws IOException {
        ImageIO.write(bimage, "png", out);
    }

    public void start() throws IOException {
        countIPs();

        bimage = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bimage.createGraphics();

        // Set background to black
        g2d.setColor(new Color(0, 0, 0));
        g2d.fillRect(0, 0, imageWidth, imageHeight);

        drawIPs();

        // Transparent white for drawing the labels on top of the heatmap
        g2d.setColor(new Color(255, 255, 255, 120));

        FileReader reader = new FileReader(labelFile);
        List<Annotation> annotations = Annotate.readLabelFile(reader);
        reader.close();
        drawLabels(g2d, annotations);
    }

    private void drawIPs() {
        double logB = maxCount == 1 ? 1 : Math.log(maxCount);
        double logC = 255.0 / logB;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                int k = ipCounts[x][y];
                if (k > 0) {
                    k = (int) (logC * Math.log(k));
                    k = Math.min(k, colors.length - 1);
                    bimage.setRGB(x, y, colors[k].getRGB());
                }
            }
        }
    }

    /**
     * Reads all of the loaded IP lists and stores the count of each IP block
     * into a two dimensional array which will later be scaled and written to
     * the image.
     * 
     * @throws IOException
     */
    private void countIPs() throws IOException {
        for (IPMapping iplist : iplists) {
            BBox ipBox = bboxUtil.boundingBox(iplist.getRange());
            if (subset.overlaps(ipBox)) {
                FileInputStream fileIn = new FileInputStream(iplist.getIpFile());
                DataInputStream in = new DataInputStream(fileIn);

                try {
                    while (true) {
                        long ip = in.readLong();
                        ip >>= bitsPerPixel;
                        Point p = Hilbert.getPoint(ip, hilbertOrder);
                        remapPoint(p);
                        if (pointInImage(p)) {
                            ipCounts[p.x][p.y]++;
                            if (ipCounts[p.x][p.y] > maxCount) {
                                maxCount = ipCounts[p.x][p.y];
                            }
                        }
                    }
                } catch (EOFException ex) {
                }
                in.close();
            }
        }
    }

    /**
     * Checks if the given point is contained within the image.
     * 
     * @param p
     * @return
     */
    private boolean pointInImage(Point p) {
        return p.x < imageWidth && p.x >= 0 && p.y < imageHeight && p.y >= 0;
    }

    private void remapPoint(Point p) {
        p.x -= offset.x;
        p.y -= offset.y;
    }

    private void drawLabels(Graphics2D g2d, List<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            BBox bbox = bboxUtil.boundingBox(annotation.getCidr());

            if (subset.overlaps(bbox)) {
                bbox.remap(offset);

                int width = bbox.getXmax() - bbox.getXmin();
                int height = bbox.getYmax() - bbox.getYmin();
                g2d.drawRect(bbox.getXmin(), bbox.getYmin(), width, height);

                String sublabel = annotation.getSublabel();

                BBox txtBBox = new BBox(bbox);

                if (!sublabel.isEmpty()) {
                    txtBBox.setYmax(txtBBox.getYmax() - (height / 3));
                }

                txtBBox.shrink(3, 2);

                Font font = getFont(g2d, txtBBox, annotation.getLabel(), 128);
                if (font != null) {
                    g2d.setFont(font);
                    g2d.drawString(annotation.getLabel(), txtBBox.getXmin(),
                            txtBBox.getYmax());
                }

                if (!sublabel.isEmpty()) {
                    BBox subBBox = new BBox(bbox);
                    subBBox.setYmin(txtBBox.getYmax());
                    subBBox.shrink(2, 1);

                    String text = annotation.getSublabel();
                    Font subFont = getFont(g2d, subBBox, text, 12);
                    if (subFont != null) {
                        g2d.setFont(subFont);
                        g2d.drawString(text, subBBox.getXmin(),
                                subBBox.getYmax());
                    }
                }
            }
        }
    }

    private Font getFont(Graphics2D g2d, BBox bbox, String text, int fontSize) {
        int bboxWidth = bbox.getXmax() - bbox.getXmin();
        int bboxHeight = bbox.getYmax() - bbox.getYmin();

        Font font;
        int txtWidth;
        int txtHeight;

        int minFontSize = 5;

        do {
            fontSize -= 1;
            font = new Font("SansSerif", Font.PLAIN, fontSize);
            FontMetrics metrics = g2d.getFontMetrics(font);
            txtWidth = metrics.stringWidth(text);
            txtHeight = metrics.getHeight();
        } while ((txtWidth > bboxWidth || txtHeight > bboxHeight)
                && fontSize > minFontSize);

        if (fontSize <= minFontSize) {
            font = null;
        }

        int xOffset = (bboxWidth - txtWidth) / 2;
        int yOffset = (bboxHeight - txtHeight) / 2;

        bbox.shrink(xOffset, yOffset);

        return font;
    }

    public Point getOffset() {
        return offset;
    }

    public File getLabelFile() {
        return labelFile;
    }

    public void setLabelFile(File labelFile) {
        this.labelFile = labelFile;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        long now = System.currentTimeMillis();
        CIDR cidr = new CIDR("0.0.0.0/2");
        String file = "/home/rafael/crawler/hubs.txt";
        IPMap h = new IPMap(cidr, 4);
        h.setLabelFile(new File("/home/rafael/crawler/network-labels.txt"));
        h.addIPMapping(new IPMapping(new CIDR("0.0.0.0/0"), new File(file)));
        h.start();
        h.saveImage(new File("test.png"));

        System.out.println((System.currentTimeMillis() - now) / 1000.0);
    }

}
