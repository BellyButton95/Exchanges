package OrderBooks;

import Exchanges.Exchange;
import Utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderBook {

    Logger logger = LoggerFactory.getLogger(OrderBook.class);

    private Map<String, GenericOrderBook> orderBookMap;
    private Map<Double, Double> allBids;
    private Map<Double, Double> allAsks;
    private List<Double> allSortedBids;
    private List<Double> allSortedAsks;
    private double bestBid;
    private double bestAsk;

    public OrderBook(Exchange exchange1, Exchange exchange2) {
        this.bestBid = Double.NaN;
        this.bestAsk = Double.NaN;
        this.allBids = new ConcurrentHashMap<>();
        this.allAsks = new ConcurrentHashMap<>();
        this.allSortedBids = new CopyOnWriteArrayList<>();
        this.allSortedAsks = new CopyOnWriteArrayList<>();
        this.orderBookMap = new ConcurrentHashMap<>(2);
        this.orderBookMap.put(exchange1.getId(), new GenericOrderBook());
        this.orderBookMap.put(exchange2.getId(), new GenericOrderBook());
        exchange1.getOrderBook().addListener(this);
        exchange2.getOrderBook().addListener(this);
    }

    public synchronized void onUpdate(String id, List<Double> newSortedBids, Map<Double, Double> newBids, List<Double> newSortedAsks, Map<Double, Double> newAsks) {
        if(!orderBookMap.containsKey(id)) {
            logger.warn("Order book with id: {} missing", id);
            return;
        }

        GenericOrderBook bookToUpdate = orderBookMap.get(id);
        GenericOrderBook otherBook = orderBookMap.get(orderBookMap.keySet().stream().filter(k -> !k.equals(id)).findFirst().get());
        bookToUpdate.clearOrderBook();
        bookToUpdate.updateOrderBook(newSortedBids, newSortedAsks, newBids, newAsks);
        clearOrderBook();

        CollectionUtils.mergeTwoSortedArrays(bookToUpdate.sortedBids, bookToUpdate.bidsMap, otherBook.sortedBids, otherBook.bidsMap, allSortedBids, allBids, CollectionUtils.BID_COMPERATOR);
        CollectionUtils.mergeTwoSortedArrays(bookToUpdate.sortedAsks, bookToUpdate.asksMap, otherBook.sortedAsks, otherBook.asksMap, allSortedAsks, allAsks, CollectionUtils.ASK_COMPERATOR);

        bestBid = allSortedBids.get(0);
        bestAsk = allSortedAsks.get(0);
        logOrderBook();
    }

    private void clearOrderBook() {
        allSortedBids.clear();
        allSortedAsks.clear();
        allBids.clear();
        allAsks.clear();
    }

    private void logOrderBook() {
        logger.info("---------------------------------------------------------------------------------");
        for(int i = allSortedAsks.size() - 1; i >= 0; i--) {
            double price = allSortedAsks.get(i);
            logger.info("Ask: {} | Qty: {}", price, allAsks.get(price));
        }
        logger.info("Best Ask: {}", bestAsk);
        logger.info("Best Bid: {}", bestBid);
        allSortedBids.forEach(b -> logger.info("Bid: {} | Qty: {}", b, allBids.get(b)));
        logger.info("---------------------------------------------------------------------------------");
    }

    public List<Double> getAllSortedBids() {
        return allSortedBids;
    }

    public List<Double> getAllSortedAsks() {
        return allSortedAsks;
    }

    public double getBestBid() {
        return bestBid;
    }

    public double getBestAsk() {
        return bestAsk;
    }

    public Map<Double, Double> getAllBids() {
        return allBids;
    }

    public Map<Double, Double> getAllAsks() {
        return allAsks;
    }

    private class GenericOrderBook {
        private List<Double> sortedBids;
        private List<Double> sortedAsks;
        private Map<Double, Double> bidsMap;
        private Map<Double, Double> asksMap;

        public GenericOrderBook() {
            this.sortedAsks = new CopyOnWriteArrayList<>();
            this.sortedBids = new CopyOnWriteArrayList<>();
            this.bidsMap = new ConcurrentHashMap<>();
            this.asksMap = new ConcurrentHashMap<>();
        }

        public void clearOrderBook() {
            sortedBids.clear();
            sortedAsks.clear();
            bidsMap.clear();
            asksMap.clear();
        }

        public void updateOrderBook(List<Double> newSortedBids, List<Double> newSortedAsks, Map<Double, Double> newBids, Map<Double, Double> newAsks) {
            sortedBids.addAll(newSortedBids);
            sortedAsks.addAll(newSortedAsks);
            bidsMap.putAll(newBids);
            asksMap.putAll(newAsks);
        }
    }

}
