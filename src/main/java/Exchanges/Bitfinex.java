package Exchanges;

import OrderBooks.BitfinexOrderBook;
import Utils.IOUtils;

public class Bitfinex extends Exchange {

    private final static String ID = "Bitfinex";

    public Bitfinex() {
        super(ID, new BitfinexOrderBook(ID));
    }

    public Bitfinex(BitfinexOrderBook orderBook) {
        super(ID, orderBook);
    }

    @Override
    public String getURI() {
        return IOUtils.getProperty("bitfinexURI");
    }


}
