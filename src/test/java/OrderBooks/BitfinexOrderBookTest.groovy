package OrderBooks

import junit.framework.TestCase

import java.util.function.Function
import java.util.stream.Collectors

class BitfinexOrderBookTest extends TestCase {

    private BitfinexOrderBook orderBook = new BitfinexOrderBook("Bitfinex")
    private CharSequence snapshot = "[1,[[5.5,1,5.5],[4.5,1,4.5],[6.5,1,-6.5],[7,1,-7]]]"
    private CharSequence updateBid = "[1,[6.5,1,6.5]]"
    private CharSequence removeBid = "[1,[6.5,0,1]]"
    private CharSequence updateBidQty = "[1,[5.5,1,10]]"
    private CharSequence updateAsk = "[1,[5.5,1,-5.5]]"
    private CharSequence removeAsk = "[1,[5.5,0,-1]]"
    private CharSequence updateAskQty = "[1,[6.5,1,-10]]"
    private CharSequence testSnapshot = "{test:test}"
    private List<Double> expectedSortedBids = List.of(5.5d, 4.5d)
    private Map<Double, Double> expectedBids = expectedSortedBids.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))
    private List<Double> expectedSortedAsks = List.of(6.5d, 7d)
    private Map<Double, Double> expectedAsks = expectedSortedAsks.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))

    void testOnInit() {
        orderBook.onInit(testSnapshot)
        assertTrue(orderBook.getSortedBids().isEmpty())
        assertTrue(orderBook.getSortedAsks().isEmpty())
        assertTrue(orderBook.getBidsMap().isEmpty())
        assertTrue(orderBook.getBidsMap().isEmpty())

        orderBook.onInit(snapshot)
        for(int i = 0; i < expectedSortedBids.size(); i++) {
            assertEquals(orderBook.getSortedBids().get(i), expectedSortedBids.get(i))
        }
        for(Double key : expectedBids.keySet()) {
            assertEquals(orderBook.getBidsMap().get(key), expectedBids.get(key))
        }
        for(int i = 0; i < expectedSortedAsks.size(); i++) {
            assertEquals(orderBook.getSortedAsks().get(i), expectedSortedAsks.get(i))
        }
        for(Double key : expectedAsks.keySet()) {
            assertEquals(orderBook.getAsksMap().get(key), expectedAsks.get(key))
        }
    }

    void testOnUpdate() {
        orderBook.onInit(snapshot)

        orderBook.onUpdate(updateBid)
        assertEquals(orderBook.getSortedBids().get(0), 6.5d)
        assertEquals(orderBook.getBidsMap().get(6.5d), 6.5d)
        orderBook.onUpdate(removeBid)
        assertFalse(orderBook.getSortedBids().contains(6.5d))
        assertNull(orderBook.getBidsMap().get(6.5d))
        orderBook.onUpdate(updateBidQty)
        assertEquals(orderBook.getSortedBids().get(0), 5.5d)
        assertEquals(orderBook.getBidsMap().get(5.5d), 10d)

        orderBook.onUpdate(updateAsk)
        assertEquals(orderBook.getSortedAsks().get(0), 5.5d)
        assertEquals(orderBook.getAsksMap().get(5.5d), 5.5d)
        orderBook.onUpdate(removeAsk)
        assertFalse(orderBook.getSortedAsks().contains(5.5d))
        assertFalse(orderBook.getAsksMap().containsKey(5.5d))
        orderBook.onUpdate(updateAskQty)
        assertEquals(orderBook.getSortedAsks().get(0), 6.5d)
        assertEquals(orderBook.getAsksMap().get(6.5d), 10d)
    }
}
