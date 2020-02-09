package OrderBooks

import Exchanges.ExchangeFactory
import junit.framework.TestCase

class ExchangeOrderBookTest extends TestCase {

    private OrderBook book = new OrderBook(ExchangeFactory.getExchange("Kraken"), ExchangeFactory.getExchange("Bitfinex"))
    private KrakenOrderBook orderBook = new KrakenOrderBook("Kraken")

    void testIsInitialized() {
        assertFalse(orderBook.isInitialized())
    }

    void testAddListener() {
        assertTrue(orderBook.getListeners().isEmpty())
        orderBook.addListener(book)
        assertTrue(orderBook.getListeners().size() > 0)
    }
}
