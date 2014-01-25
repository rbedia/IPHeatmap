IPHeatmap
=========

IPHeatmap is a tool for creating interactive IPv4 heatmaps. It allows zooming and panning to get a more detailed view of the IPv4 address space.

This project was inspired by, and based on, the [ipv4-heatmap](http://maps.measurement-factory.com/) tool.

It maps the IPv4 address space into a two dimensional image using a [Hilbert curve](http://en.wikipedia.org/wiki/Hilbert_curve).

Usage
-----

Build IPHeatmap.

    cd IPHeatmap
    mvn package

Unpack the bundle.

    mkdir /tmp/ipheatmap
    tar jxCf /tmp/ipheatmap ipheatmap-assembly/target/ipheatmap-assembly-*-bundle.tar.bz2

Change to the ipheatmap directory.

    cd /tmp/ipheatmap

Create a text file named sample.txt with 1 IP address per line. For example:

    10.0.0.1
    192.168.0.2
    127.0.0.1

You will want to have a fairly large data set on the order of 100,000 IP addresses. Or if your data is packed into specific IP blocks then a smaller number of addresses would still produce interesting results.

Use the splitter tool to convert the input IP list into a format that can be used more efficiently by the tiling tool. The second argument is the directory where the data will be stored.

    java -jar ipheatmap-splitter-0.0.1-SNAPSHOT-run.jar sample.txt data

Generate tiles using the tiler tool. tiles is the directory where the image files are stored. network-labels.txt is a file containing network blocks with annotations. They will show as overlays on the tile map. Lastly data is the directory of IP data that is used to create the tile images.

    java -jar ipheatmap-tiler-0.0.1-SNAPSHOT-run.jar -o tiles -l network-labels.txt -i data

Open /tmp/ipheatmap/index.html in a web browser to see the result.
