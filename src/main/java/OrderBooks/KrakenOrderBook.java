package OrderBooks;

import Utils.CollectionUtils;
import Utils.NumberUtils;
import Utils.OrderBookDeserializer;
import Utils.Side;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class KrakenOrderBook extends ExchangeOrderBook {

    private final static Logger logger = LoggerFactory.getLogger(KrakenOrderBook.class);

    public KrakenOrderBook(String id){
        super(id, new KrakenBookSubscribeDeserializer(), new KrakenBookUpdateDeserializer());
    }

    @Override
    public void onInit(CharSequence data) {
        KrakenBookSubscribe book  = gson.fromJson(data.toString(), KrakenBookSubscribe.class);
        if(book.isValid()) {
            createCache(book.getAsks(), sortedAsks, asksMap);
            createCache(book.getBids(), sortedBids, bidsMap);
            logger.debug("Updated order book: Bids: {} | Asks: {}", sortedBids, sortedAsks);
            orderBooks.forEach(e -> e.onUpdate(id, ImmutableList.copyOf(sortedBids), ImmutableMap.copyOf(bidsMap), ImmutableList.copyOf(sortedAsks), ImmutableMap.copyOf(asksMap)));
        }
    }

    private synchronized void createCache(Double[][] prices, List<Double> sortedPrices, Map<Double, Double> pricesMap) {
        sortedPrices.clear();
        pricesMap.clear();
        for(int i = 0; i < prices.length; i++) {
            double price = prices[i][0];
            double qty = prices[i][1];
            if(NumberUtils.isValidQty(qty) && NumberUtils.isValidPrice(price)) {
                sortedPrices.add(price);
                pricesMap.put(price, qty);
            }
        }
    }

    @Override
    public void onUpdate(CharSequence data) {
        KrakenBookUpdate update = gson.fromJson(data.toString(), KrakenBookUpdate.class);
        if (update.isValid()) {
            if (update.getSide() == Side.Bid) {
                updateCache(update.getUpdate(), sortedBids, bidsMap, Side.Bid);
            } else {
                updateCache(update.getUpdate(), sortedAsks, asksMap, Side.Ask);
            }
            logger.debug("Updated order book: Bids: {} | Asks: {}", sortedBids, sortedAsks);
            orderBooks.forEach(e -> e.onUpdate(id, ImmutableList.copyOf(sortedBids), ImmutableMap.copyOf(bidsMap), ImmutableList.copyOf(sortedAsks), ImmutableMap.copyOf(asksMap)));
        }

    }

    private synchronized void updateCache(Double[][] update, List<Double> sortedPrices, Map<Double, Double> pricesMap, Side side) {
        for (int i = 0; i < update.length; i++) {
            double price = update[i][0];
            double qty = update[i][1];
            if (pricesMap.containsKey(price)) {
                if (NumberUtils.isValidQty(qty) && NumberUtils.isValidPrice(price)) {
                    pricesMap.replace(price, qty);
                } else {
                    pricesMap.remove(price);
                    sortedPrices.remove(price);
                }
            } else {
                if(NumberUtils.isValidQty(qty) && NumberUtils.isValidPrice(price)) {
                    pricesMap.put(price, qty);
                    sortedPrices.add(price);
                }
            }
        }
        if (side == Side.Bid) {
            sortedPrices.sort(CollectionUtils.BID_COMPERATOR);
        } else {
            sortedPrices.sort(CollectionUtils.ASK_COMPERATOR);
        }
    }

    private static class KrakenBookSubscribe {
        private final Double[][] as;
        private final Double[][] bs;

        public KrakenBookSubscribe(Double[][] as, Double[][] bs){
            this.as = as;
            this.bs = bs;
        }

        public Double[][] getAsks() {
            return as;
        }

        public Double[][] getBids() {
            return bs;
        }

        public boolean isValid() { return as != null && bs != null; }
    }

    private static class KrakenBookUpdate {
        private final String[][] a;
        private final String[][] b;

        public KrakenBookUpdate(String[][] a, String[][] b){
            this.a = a;
            this.b = b;
        }

        public Double[][] getUpdate() {
            return a != null ? convertUpdate(a) : convertUpdate(b);
        }

        public Side getSide() {
            return a != null ? Side.Ask : Side.Bid;
        }

        public boolean isValid() { return a != null || b != null; }

        private Double[][] convertUpdate(String[][] prices) {
            Double[][] doublePrices = new Double[prices.length][2];
            for(int i = 0; i < prices.length; i++) {
                doublePrices[i][0] = Double.valueOf(prices[i][0]);
                doublePrices[i][1] = Double.valueOf(prices[i][1]);
            }
            return doublePrices;
        }

    }

    private static class KrakenBookSubscribeDeserializer implements OrderBookDeserializer<KrakenBookSubscribe> {

        private final static KrakenBookSubscribe EMPTY_SUBSCRIBE = new KrakenBookSubscribe(null, null);
        private final static Gson GSON = new Gson();

        @Override
        public KrakenBookSubscribe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonArray) {
                Object[] response = GSON.fromJson(json, Object[].class);
                return GSON.fromJson(response[1].toString(), KrakenBookSubscribe.class);
            }
            return EMPTY_SUBSCRIBE;
        }

        @Override
        public Type getType() {
            return KrakenBookSubscribe.class;
        }
    }

    private static class KrakenBookUpdateDeserializer implements OrderBookDeserializer<KrakenBookUpdate> {

        private final static KrakenBookUpdate EMPTY_UPDATE = new KrakenBookUpdate(null, null);
        private final static Gson GSON = new Gson();

        @Override
        public KrakenBookUpdate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonArray) {
                Object[] response = GSON.fromJson(json, Object[].class);
                return GSON.fromJson(response[1].toString(), KrakenBookUpdate.class);
            }
            return EMPTY_UPDATE;
        }

        @Override
        public Type getType() {
            return KrakenBookUpdate.class;
        }
    }

}
