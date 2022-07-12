class Money:
    def __init__(self, amount: int):
        self.amount = amount

    @classmethod
    def dollars(cls, amount: int) -> 'Money':
        return cls(amount)

    def add(self, another: 'Money'):
        """add money from another money instance

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
