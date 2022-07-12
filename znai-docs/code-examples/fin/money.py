class Money:
    def __init__(self, amount: int):
        self.amount = amount

    @classmethod
    def dollars(cls, amount: int):
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


def print_money(amount: Money, message: str = ""):
    """
    print money with a given message

    Parameters
    --------------
    amount:
      amount to print

    message:
      message to use for audit
    """

    print("printing money")
