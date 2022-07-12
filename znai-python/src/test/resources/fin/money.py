class Money:
    def __init__(self, amount: int, currency: str):
        """
        generic constructor that accepts a currency

        Parameters
        ----------
        amount:
          amount of money

        currency:
          currency to associate with the created Money
        """
        self.amount = amount
        self.currency = currency

    @classmethod
    def dollars(cls, amount: int) -> 'Money':
        """
        creates money instance with `USD` as the currency

        Parameters
        ----------
        amount:
          dollar amount

        Returns
        -------
          new money instance with `USD` as currency
        """
        return cls(amount, "USD")

    def add(self, another: 'Money'):
        """
        add money from another money instance

        Parameters
        --------------
        another :
          money to add
        """
        self.amount += another.amount


class Debt:
    def __init__(self, amount: Money):
        self.amount = amount


def render_money(amount: Money, message: str = "") -> str:
    """
    render money to a string

    Parameters
    --------------
    amount:
      amount to print

    message:
      message to use for audit

    Returns
    -------
      money represented as text
    """

    return "printing money"
