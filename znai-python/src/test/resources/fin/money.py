class Money:
    def __init__(self, amount: int):
        self.amount = amount

    def add(self, another: 'Money'):
        self.amount += another.amount


class Debt:
    def __init__(self, amount: Money):
        self.amount = amount


def print_money():
    print("printing money")