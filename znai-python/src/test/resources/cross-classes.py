import string
from typing import Union, Dict

from fin.money import Money, Debt
from fin.money import Money as SuperMoney

import fin.money as finm

class Context:
    def clear(self):
        pass


class Transaction:
    def __init__(self):
        self.data = "hello"

    def execute(self,
                opts: Dict[string, finm.Money],
                context: Union[Context, list[Context], string],
                some_other: Money):
        print("execute: " + self.data)
        pass

    def cancel(self, context: Dict[SuperMoney, Debt]):
        pass
