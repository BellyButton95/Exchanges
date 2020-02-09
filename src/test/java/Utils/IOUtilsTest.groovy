package Utils

import junit.framework.TestCase

class IOUtilsTest extends TestCase {

    void testGetProperty() {
        assertNotNull(IOUtils.getProperty("krakenBTCPair"));
        assertNull(IOUtils.getProperty("randomBTCPair"));
    }
}
