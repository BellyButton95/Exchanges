package Connectivity


import junit.framework.TestCase

class BitfinexSubscriptionRequestTest extends TestCase {

    private String expectedPayload = "{\"event\":\"subscribe\",\"channel\":\"book\",\"symbol\":\"BTCUSD\",\"prec\":\"P0\",\"len\":\"25\"}"

    void testGetPayload() {
        BitfinexSubscriptionRequest request = new BitfinexSubscriptionRequest("book", "BTCUSD", "P0", "25")
        assertEquals(request.getPayload(), expectedPayload)
    }
}
