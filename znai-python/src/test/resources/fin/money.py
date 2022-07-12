class Money:
    """
    Money representation.
    Encapsulates money type, combining amount and currency. Deals with precision errors.

    Warning: avoid using `int` in place of money and use this type instead
    """

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
        self._amount = amount
        self._currency = currency

    @property
    def amount(self) -> int:
        """
        amount in provided currency
        """
        return self._amount

    @amount.setter
    def amount(self, amount: int):
        self.amount = amount

    @property
    def currency(self) -> str:
        """
        money currency
        """
        return self._currency

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

    return f"{message} {amount.amount} {amount.currency}"
