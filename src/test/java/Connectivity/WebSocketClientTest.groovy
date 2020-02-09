package Connectivity

import Exchanges.Exchange
import junit.framework.TestCase
import org.mockito.Mockito

import java.net.http.WebSocket

class WebSocketClientTest extends TestCase {

    private Exchange exchange = Mockito.mock(Exchange.class)
    private WebSocket webSocket = Mockito.mock(WebSocket.class)
    private WebSocketClient client = new WebSocketClient(exchange)

    void testOnOpen() {
        Mockito.when(exchange.getPayload()).thenReturn("test")
        client.onOpen(webSocket)
        Mockito.verify(webSocket).sendText("test", true)

    }

    void testOnText() {
        client.onText(webSocket, "data", false)
        Mockito.verify(exchange).onEvent(Mockito.any(CharSequence.class))
    }
}
