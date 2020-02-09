package Connectivity


import junit.framework.TestCase

class KrakenSubscriptionRequestTest extends TestCase {

    private String expectedPayload = "{\"event\":\"subscribe\",\"pair\":[\"XBTUSD\"],\"subscription\":{\"name\":\"book\",\"depth\":25}}"

    void testGetPayload() {
        String[] pairs = new String[1]
        pairs[0] = "XBTUSD"
        KrakenSubscriptionRequest request = new KrakenSubscriptionRequest(pairs,
                new KrakenSubscriptionRequest.KrakenSubscriptionElement("book", 25))
        assertEquals(request.getPayload(), expectedPayload)
    }
}
