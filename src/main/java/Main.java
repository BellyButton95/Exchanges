import Connectivity.WebSocketClient;
import Exchanges.Exchange;
import Exchanges.ExchangeFactory;
import OrderBooks.OrderBook;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Exchange bitfinex = ExchangeFactory.getExchange("Bitfinex");
        Exchange kraken = ExchangeFactory.getExchange("Kraken");
        OrderBook orderBookListener = new OrderBook(kraken, bitfinex);

        HttpClient httpClientBitfinex = HttpClient.newBuilder().executor(executor).build();
        WebSocket.Builder webSocketBuilderBitfinex= httpClientBitfinex.newWebSocketBuilder();
        WebSocket webSocketBitfinex = webSocketBuilderBitfinex.buildAsync(URI.create(bitfinex.getURI()), new WebSocketClient(bitfinex)).join();

        HttpClient httpClientKraken = HttpClient.newBuilder().executor(executor).build();
        WebSocket.Builder webSocketBuilderKraken = httpClientKraken.newWebSocketBuilder();
        WebSocket webSocketKraken = webSocketBuilderKraken.buildAsync(URI.create(kraken.getURI()), new WebSocketClient(kraken)).join();

    }
}
