package Connectivity;

import Exchanges.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;

public class WebSocketClient implements WebSocket.Listener{

    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);
    private Exchange listener;

    public WebSocketClient(Exchange listener) {
        this.listener = listener;
    }

    public void onOpen(WebSocket webSocket) {
        logger.info("Connected to: {}", listener.getId());
        webSocket.sendText(listener.getPayload(), true);
        Listener.super.onOpen(webSocket);
    }

    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        logger.debug("onText received from {} with data: {}", listener.getId(), data);
        listener.onEvent(data);
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        logger.info("Closed connection to {} with status {}, reason: {}", listener.getId(), statusCode, reason);
        return Listener.super.onClose(webSocket, statusCode, reason);
    }
}
