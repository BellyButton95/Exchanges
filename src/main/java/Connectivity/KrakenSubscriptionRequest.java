package Connectivity;

import com.google.gson.Gson;

public class KrakenSubscriptionRequest implements SubscriptionRequest {
    /*
        {
          "event": "subscribe",
          "pair": [
            "XBT/USD",
            "XBT/EUR"
          ],
          "subscription": {
            "name": "book"
          }
        }
     */
    private final String event;
    private final String[] pair;
    private final KrakenSubscriptionElement subscription;
    private final transient Gson gson;

    public KrakenSubscriptionRequest(final String[] pair, final KrakenSubscriptionElement subscription) {
        this.event = "subscribe";
        this.pair = pair;
        this.subscription = subscription;
        this.gson = new Gson();
    }

    @Override
    public CharSequence getPayload(){
        return gson.toJson(this);
    }

    public static class KrakenSubscriptionElement{
        private final String name;
        private final int depth;

        public KrakenSubscriptionElement(final String name, final int depth) {
            this.name = name;
            this.depth = depth;
        }
    }
}
