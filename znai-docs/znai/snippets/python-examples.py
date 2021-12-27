# example-import-block
import market
# example-import-block

def main():
    # example: book trade
    id = market.book_trade('symbol', market.CURRENT_PRICE, 100)
    # example-end

    # example-cancel-trade
    market.cancel_trade(id)
    # example-cancel-trade

if __name__  == "__main__":
    main()