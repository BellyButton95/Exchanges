package Exchanges;

import OrderBooks.KrakenOrderBook;
import Utils.IOUtils;

public class Kraken extends Exchange {

    private final static String ID = "Kraken";

    public Kraken() {
        super(ID, new KrakenOrderBook(ID));
    }

    public Kraken(KrakenOrderBook orderBook) {
        super(ID, orderBook);
    }

    @Override
    public String getURI() {
        return IOUtils.getProperty("krakenURI");
    }
}
