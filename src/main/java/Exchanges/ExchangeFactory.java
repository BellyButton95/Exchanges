package Exchanges;

public class ExchangeFactory {

    public static Exchange getExchange(String exchange) {
        return Exchanges.valueOf(exchange).createExchange();
    }

    private enum Exchanges {
        Kraken{
            @Override
            public Exchange createExchange() {
                return new Kraken();
            }
        },
        Bitfinex{
            @Override
            public Exchange createExchange() {
                return new Bitfinex();
            }
        };

        public abstract Exchange createExchange();
    }
}
