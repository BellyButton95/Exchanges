package Connectivity;

import Utils.IOUtils;

public class SubscriptionRequestFactory {

    public static SubscriptionRequest getSubsriptionRequest(String exchange) {
        return SubscriptionRequests.valueOf(exchange).getSubscriptionRequest();
    }

    public enum SubscriptionRequests{
        Kraken{
            @Override
            public SubscriptionRequest getSubscriptionRequest() {
                return new KrakenSubscriptionRequest(new String[]{IOUtils.getProperty("krakenBTCPair")},
                        new KrakenSubscriptionRequest.KrakenSubscriptionElement(IOUtils.getProperty("krakenChannel"),
                                Integer.parseInt(IOUtils.getProperty("krakenDepth"))));
            }
        },
        Bitfinex{
            @Override
            public SubscriptionRequest getSubscriptionRequest() {
                return new BitfinexSubscriptionRequest(IOUtils.getProperty("bitfinexChannel"),
                        IOUtils.getProperty("bitfinexBTCPair"), "P0", IOUtils.getProperty("bitfinexDepth"));
            }
        };
        public abstract SubscriptionRequest getSubscriptionRequest();
    }
}
