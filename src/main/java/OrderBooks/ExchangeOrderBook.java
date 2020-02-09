package OrderBooks;

import Utils.OrderBookDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ExchangeOrderBook {

    final protected Gson gson;
    final protected String id;

    protected List<Double> sortedBids;
    protected List<Double> sortedAsks;
    protected Map<Double, Double> bidsMap;
    protected Map<Double, Double> asksMap;
    protected Collection<OrderBook> orderBooks;
    
    public ExchangeOrderBook(String id, OrderBookDeserializer subscribeDeserializer, OrderBookDeserializer updateDeserializer) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(subscribeDeserializer.getType(), subscribeDeserializer);
        gsonBuilder.registerTypeAdapter(updateDeserializer.getType(), updateDeserializer);
        this.id = id;
        this.gson = gsonBuilder.create();
        this.orderBooks = new ArrayList<>();
        this.sortedAsks = new CopyOnWriteArrayList<>();
        this.sortedBids = new CopyOnWriteArrayList<>();
        this.bidsMap = new ConcurrentHashMap<>();
        this.asksMap = new ConcurrentHashMap<>();
    }

    public boolean isInitialized() {
        return !sortedBids.isEmpty() && !sortedAsks.isEmpty();
    }

    public void onInit(CharSequence data){
        throw new UnsupportedOperationException("Provide implementation for onInit");
    }

    public void onUpdate(CharSequence data){
        throw new UnsupportedOperationException("Provide implementation for onUpdate");
    }

    public void addListener(OrderBook orderBook) {
        orderBooks.add(orderBook);
    }

    public Collection<OrderBook> getListeners() {
        return orderBooks;
    }

    public List<Double> getSortedBids() {
        return sortedBids;
    }

    public List<Double> getSortedAsks() {
        return sortedAsks;
    }

    public Map<Double, Double> getBidsMap() {
        return bidsMap;
    }

    public Map<Double, Double> getAsksMap() {
        return asksMap;
    }
}
