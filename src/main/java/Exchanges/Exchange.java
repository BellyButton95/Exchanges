package Exchanges;

import Connectivity.SubscriptionRequestFactory;
import OrderBooks.ExchangeOrderBook;
import Connectivity.SubscriptionRequest;

public abstract class Exchange {

    protected final String id;

    protected ExchangeOrderBook orderBook;
    protected SubscriptionRequest request;

    public Exchange(String id, ExchangeOrderBook orderBook){
        this.id = id;
        this.orderBook = orderBook;
        this.request = SubscriptionRequestFactory.getSubsriptionRequest(id);
    }

    public synchronized void onEvent(CharSequence data) {
        if (orderBook.isInitialized()) {
            orderBook.onUpdate(data);
        } else {
            orderBook.onInit(data);
        }
    }

    public String getId() { return id; }

    public ExchangeOrderBook getOrderBook() { return orderBook; }

    public CharSequence getPayload() {
        return request.getPayload();
    }

    public String getURI() { return null; }
}
