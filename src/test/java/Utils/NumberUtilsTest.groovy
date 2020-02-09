package Utils

import junit.framework.TestCase

class NumberUtilsTest extends TestCase {

    void testIsValidQty() {
        assertTrue(NumberUtils.isValidQty(4.0));
        assertFalse(NumberUtils.isValidQty(-4.0));
        assertFalse(NumberUtils.isValidQty(0.0));
    }

    void testIsValidQtyInclNegative() {
        assertTrue(NumberUtils.isValidQtyInclNegative(4.0));
        assertTrue(NumberUtils.isValidQtyInclNegative(-4.0));
        assertFalse(NumberUtils.isValidQtyInclNegative(0.0));
    }

    void testIsValidPrice() {
        assertTrue(NumberUtils.isValidPrice(4.0));
        assertFalse(NumberUtils.isValidPrice(-4.0));
        assertFalse(NumberUtils.isValidPrice(0.0));
    }
}
