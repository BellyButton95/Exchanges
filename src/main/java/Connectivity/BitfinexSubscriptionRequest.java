package Connectivity;

import com.google.gson.Gson;

public class BitfinexSubscriptionRequest implements SubscriptionRequest {

    /*
    {
        "event":"subscribed",
        "channel":"book",
        "symbol":"tBTCUSD",
        "prec":"P0",
     }
     */

    private final String event;
    private final String channel;
    private final String symbol;
    private final String prec;
    private final String len;
    private final transient Gson gson;

    public BitfinexSubscriptionRequest(final String channel, final String symbol, final String prec, final String len) {
        this.event = "subscribe";
        this.channel = channel;
        this.symbol = symbol;
        this.prec = prec;
        this.len = len;
        this.gson = new Gson();
    }

    @Override
    public CharSequence getPayload() { return gson.toJson(this); }
}


