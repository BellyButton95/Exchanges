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

public class BitfinexOrderBook extends ExchangeOrderBook {

    private final static Logger logger = LoggerFactory.getLogger(BitfinexOrderBook.class);

    public BitfinexOrderBook(String id){
        super(id, new BitfinexBookSubscirbeDeserializer(), new BitfinexBookUpdateDeserializer());
    }

    @Override
    public void onInit(CharSequence data) {
        Double[][] response = gson.fromJson(data.toString(), Double[][].class);
        if(response.length > 0) {
            createCache(response);
            logger.debug("Updated order book: Bids: {} | Asks: {}", sortedBids, sortedAsks);
            orderBooks.forEach(e -> e.onUpdate(id, ImmutableList.copyOf(sortedBids), ImmutableMap.copyOf(bidsMap), ImmutableList.copyOf(sortedAsks), ImmutableMap.copyOf(asksMap)));
        }
    }

    private void createCache(Double[][] prices) {
        sortedBids.clear();
        sortedAsks.clear();
        bidsMap.clear();
        asksMap.clear();
        for(int i = 0; i < prices.length; i++) {
            double price = prices[i][0];
            double qty = prices[i][2];
            if(NumberUtils.isValidQtyInclNegative(qty) && NumberUtils.isValidPrice(price)) {
                if(qty > 0) {
                    sortedBids.add(price);
                    bidsMap.put(price, qty);
                } else {
                    sortedAsks.add(price);
                    asksMap.put(price, Math.abs(qty));
                }
            }
        }
    }

    @Override
    public void onUpdate(CharSequence data) {
        Double[] update = gson.fromJson(data.toString(), Double[].class);
        if (update.length > 0) {
            if (update[1] > 0) {
                updatePrice(update);
            } else if(update[1] == 0){
                removePrice(update);
            }
            logger.debug("Updated order book: Bids: {} | Asks: {}", sortedBids, sortedAsks);
            orderBooks.forEach(e -> e.onUpdate(id, ImmutableList.copyOf(sortedBids), ImmutableMap.copyOf(bidsMap), ImmutableList.copyOf(sortedAsks), ImmutableMap.copyOf(asksMap)));
        }

    }

    private void updatePrice(Double[] update) {
        double price = update[0];
        double qty = update[2];
        if(NumberUtils.isValidQtyInclNegative(qty) && NumberUtils.isValidPrice(price)) {
            if (qty > 0) {
                if(bidsMap.containsKey(price)) {
                    bidsMap.put(price, qty);
                } else {
                    bidsMap.put(price, qty);
                    sortedBids.add(price);
                    sortedBids.sort(CollectionUtils.BID_COMPERATOR);
                }
            } else {
                if(asksMap.containsKey(price)) {
                    asksMap.put(price, Math.abs(qty));
                } else {
                    asksMap.put(price, Math.abs(qty));
                    sortedAsks.add(price);
                    sortedAsks.sort(CollectionUtils.ASK_COMPERATOR);
                }
            }
        }
    }

    private void removePrice(Double[] update) {
        Side side = update[2] == 1 ? Side.Bid : Side.Ask;
        double price = update[0];
        if(side == Side.Bid) {
            bidsMap.remove(price);
            sortedBids.remove(price);
        } else {
            asksMap.remove(price);
            sortedAsks.remove(price);
        }
    }

    private static class BitfinexBookSubscirbeDeserializer implements OrderBookDeserializer<Double[][]> {

        private static final Double[][] EMPTY_ARRAY = {};
        private static final Gson GSON = new Gson();

        @Override
        public Double[][] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonArray) {
                Object[] response = GSON.fromJson(json, Object[].class);
                Double[][] update = GSON.fromJson(response[1].toString(), Double[][].class);
                return update;
            }
            return EMPTY_ARRAY;
        }

        @Override
        public Type getType() {
            return Double[][].class;
        }
    }

    private static class BitfinexBookUpdateDeserializer implements OrderBookDeserializer<Double[]> {

        private static final Gson GSON = new Gson();
        private static final Double[] EMPTY_ARRAY = {};

        @Override
        public Double[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonArray) {
                Object[] response = GSON.fromJson(json, Object[].class);
                Double[] update = GSON.fromJson(response[1].toString(), Double[].class);
                return update;
            }
            return EMPTY_ARRAY;
        }

        @Override
        public Type getType() {
            return Double[].class;
        }
    }
}
