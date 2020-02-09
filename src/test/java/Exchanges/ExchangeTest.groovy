package Exchanges


import OrderBooks.KrakenOrderBook
import junit.framework.TestCase
import org.mockito.Mockito

class ExchangeTest extends TestCase {

    private KrakenOrderBook orderBook = Mockito.mock(KrakenOrderBook.class)
    private Exchange exchange = new Kraken(orderBook);

    void testOnEvent() {
        Mockito.when(orderBook.isInitialized()).thenReturn(false)
        exchange.onEvent("test")
        Mockito.verify(orderBook).onInit("test")
        Mockito.when(orderBook.isInitialized()).thenReturn(true)
        exchange.onEvent("test")
        Mockito.verify(orderBook).onUpdate("test")
    }
}
