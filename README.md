# Exchanges
Use config.properties to set the properties for Kraken and Bitfinex:
krakenURI=WEBSOCKET_URI_KRAKEN
bitfinexURI=WEBSOCKET_URI_BITFINEX
krakenChannel=Channel to subscribe to (book, trade, etc.)
krakenBTCPair=Pair to subscirbe to (for Kraken XBT/USD, XLM/USD, etc)
krakenDepth=Depth of order book(10, 25, etc)
bitfinexChannel=Channel to subscribe to (book, trade, etc.)
bitfinexBTCPair=Pair to subscirbe to (for Bitfinex BTCUSD, ETHUSD, etc)
bitfinexDepth=Depth of order book(for Bitfinex has to be more than 25!)

Run from Main class - can adjust log level from logback.xml in resources - by default only logs aggregated book to console and will log all messages to a log file in /logs/
