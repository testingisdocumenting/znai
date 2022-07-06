class Money:
    def __init__(self):
        self.amount = 0

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


def print_money():
    print("printing money")