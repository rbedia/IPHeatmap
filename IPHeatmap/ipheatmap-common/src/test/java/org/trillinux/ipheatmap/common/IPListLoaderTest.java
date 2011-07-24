/**
 * 
 */
package org.trillinux.ipheatmap.common;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

/**
 * @author Rafael Bedia
 * 
 */
public class IPListLoaderTest {

    /**
     * Test method for
     * {@link org.trillinux.ipheatmap.common.IPListLoader#readMappings(java.io.File)}
     * .
     * 
     * @throws URISyntaxException
     */
    @Test
    public void testReadMappings() throws URISyntaxException {
        URL url = IPListLoaderTest.class.getResource("/ipdir/index.txt");
        File indexFile = new File(url.toURI());
        File ipDir = indexFile.getParentFile();

        List<IPMapping> mappings = IPListLoader.readMappings(ipDir);
        assertEquals(19432, mappings.size());

        IPMapping mapping = mappings.get(0);
        assertEquals(new CIDR("0.0.0.0/16"), mapping.getRange());
        assertEquals(new File(ipDir, "0/0.0.0.0-16"), mapping.getIpFile());
    }

}
