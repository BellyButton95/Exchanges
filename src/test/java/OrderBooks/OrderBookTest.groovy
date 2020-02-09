package OrderBooks

import Exchanges.ExchangeFactory
import junit.framework.TestCase

import java.util.function.Function
import java.util.stream.Collectors

class OrderBookTest extends TestCase {

    private final static String KRAKEN = "Kraken"
    private final static String BITFINEX = "Bitfinex"
    private final static String TEST = "Test"

    private OrderBook orderBook = new OrderBook(ExchangeFactory.getExchange(KRAKEN), ExchangeFactory.getExchange(BITFINEX))
    private List<Double> krakenSortedBids = List.of(6d, 5d)
    private Map<Double, Double> krakenBids = krakenSortedBids.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))
    private List<Double> krakenSortedAsks = List.of(6.5d, 7d)
    private Map<Double, Double> krakenAsks = krakenSortedAsks.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))
    private List<Double> bitfinexSortedBids = List.of(5.5d, 4.5d)
    private Map<Double, Double> bitfinexBids = bitfinexSortedBids.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))
    private List<Double> bitfinexSortedAsks = List.of(6.5d, 8d)
    private Map<Double, Double> bitfinexAsks = bitfinexSortedAsks.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))
    private List<Double> expectedSortedBids = List.of(6d, 5.5d, 5d, 4.5d)
    private Map<Double, Double> expectedBids = expectedSortedBids.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))
    private List<Double> expectedSortedAsks = List.of(6.5d, 7d, 8d)
    private Map<Double, Double> expectedAsks = Map.of(6.5d, 13d, 7d, 7d, 8d, 8d)

    void testOnUpdate() {
        orderBook.onUpdate(TEST, krakenSortedBids, krakenBids, krakenSortedAsks, krakenAsks)
        assertTrue(orderBook.getAllSortedBids().isEmpty())
        assertTrue(orderBook.getAllSortedAsks().isEmpty())
        assertTrue(orderBook.getAllBids().isEmpty())
        assertTrue(orderBook.getAllAsks().isEmpty())
        assertTrue(Double.isNaN(orderBook.getBestBid()))
        assertTrue(Double.isNaN(orderBook.getBestAsk()))
        orderBook.onUpdate(KRAKEN, krakenSortedBids, krakenBids, krakenSortedAsks, krakenAsks)
        for(int i = 0; i < krakenSortedBids.size(); i++) {
            assertEquals(orderBook.getAllSortedBids().get(i), krakenSortedBids.get(i))
        }
        for(Double key : krakenBids.keySet()) {
            assertEquals(orderBook.getAllBids().get(key), krakenBids.get(key))
        }
        for(int i = 0; i < krakenSortedAsks.size(); i++) {
            assertEquals(orderBook.getAllSortedAsks().get(i), krakenSortedAsks.get(i))
        }
        for(Double key : krakenAsks.keySet()) {
            assertEquals(orderBook.getAllAsks().get(key), krakenAsks.get(key))
        }
        assertEquals(orderBook.getBestBid(), 6d)
        assertEquals(orderBook.getBestAsk(), 6.5d)
        orderBook.onUpdate(BITFINEX, bitfinexSortedBids, bitfinexBids, bitfinexSortedAsks, bitfinexAsks)
        for(int i = 0; i < expectedSortedBids.size(); i++) {
            assertEquals(orderBook.getAllSortedBids().get(i), expectedSortedBids.get(i))
        }
        for(Double key : expectedBids.keySet()) {
            assertEquals(orderBook.getAllBids().get(key), expectedBids.get(key))
        }
        for(int i = 0; i < expectedSortedAsks.size(); i++) {
            assertEquals(orderBook.getAllSortedAsks().get(i), expectedSortedAsks.get(i))
        }
        for(Double key : expectedAsks.keySet()) {
            assertEquals(orderBook.getAllAsks().get(key), expectedAsks.get(key))
        }
        assertEquals(orderBook.getBestBid(), 6d)
        assertEquals(orderBook.getBestAsk(), 6.5d)
    }
}
