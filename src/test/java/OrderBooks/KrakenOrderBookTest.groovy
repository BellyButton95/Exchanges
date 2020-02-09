package OrderBooks

import java.util.function.Function
import junit.framework.TestCase

import java.util.stream.Collectors

class KrakenOrderBookTest extends TestCase {

    private KrakenOrderBook orderBook = new KrakenOrderBook("Kraken")
    private CharSequence test = "{test:test}"
    private CharSequence snapshot = " [0,{\"as\":[[\"6.5\",\"6.5\",\"6.5\"],[\"7.0\",\"7.0\",\"7.0\"]],\"bs\":[[\"5.5\",\"5.5\",\"5.5\"],[\"4.5\",\"4.5\",\"4.5\"]]},\"book-25\",\"XBT/USD\"]"
    private CharSequence updateBid = " [0,{\"b\":[[\"6.0\",\"6.0\",\"6.0\"],[\"5.5\",\"0.0\",\"0.0\",\"r\"],[\"4.5\",\"10.5\",\"10.0\",\"r\"]]},\"book-25\",\"XBT/USD\"]"
    private CharSequence updateAsk = " [0,{\"a\":[[\"6.0\",\"6.0\",\"6.0\"],[\"7.0\",\"0.0\",\"0.0\",\"r\"],[\"6.5\",\"10.5\",\"10.0\",\"r\"]]},\"book-25\",\"XBT/USD\"]"
    private List<Double> expectedSortedBids = List.of(5.5d, 4.5d);
    private List<Double> expectedSortedAsks = List.of(6.5d, 7d);
    private Map<Double, Double> expectedBidsMap = expectedSortedBids.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
    private Map<Double, Double> expectedAsksMap = expectedSortedAsks.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));

    void testOnInit() {
        orderBook.onInit(test)
        assertTrue(orderBook.getSortedBids().isEmpty())
        assertTrue(orderBook.getSortedAsks().isEmpty())
        assertTrue(orderBook.getBidsMap().isEmpty())
        assertTrue(orderBook.getAsksMap().isEmpty())

        orderBook.onInit(snapshot)
        for(int i = 0; i < expectedSortedBids.size(); i++) {
            assertEquals(orderBook.getSortedBids().get(i), expectedSortedBids.get(i))
        }
        for(Double key : expectedBidsMap.keySet()) {
            assertEquals(orderBook.getBidsMap().get(key), expectedBidsMap.get(key))
        }
        for(int i = 0; i < expectedSortedAsks.size(); i++) {
            assertEquals(orderBook.getSortedAsks().get(i), expectedSortedAsks.get(i))
        }
        for(Double key : expectedAsksMap.keySet()) {
            assertEquals(orderBook.getAsksMap().get(key), expectedAsksMap.get(key))
        }
    }

    void testOnUpdate() {
        orderBook.onInit(snapshot)
        orderBook.onUpdate(updateBid)
        assertEquals(orderBook.getSortedBids().get(0), 6d)
        assertEquals(orderBook.getBidsMap().get(6d), 6d)
        assertFalse(orderBook.getSortedBids().contains(5.5d))
        assertFalse(orderBook.getBidsMap().containsKey(5.5d))
        assertEquals(orderBook.getBidsMap().get(4.5d), 10.5d)

        orderBook.onUpdate(updateAsk)
        assertEquals(orderBook.getSortedAsks().get(0), 6d)
        assertEquals(orderBook.getAsksMap().get(6d), 6d)
        assertFalse(orderBook.getSortedAsks().contains(7d))
        assertFalse(orderBook.getAsksMap().containsKey(7d))
        assertEquals(orderBook.getAsksMap().get(6.5d), 10.5d)
    }
}
