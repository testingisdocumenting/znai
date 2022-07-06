import string
from typing import Union, Dict

# import fin


class Context:
    def clear(self):
        pass


class Transaction:
    def __init__(self):
        self.data = "hello"

    def execute(self,
                opts: Dict[string, int],
                context: Union[Context, list[Context], string],
                some_other: fin.money.Money):
        print("execute: " + self.data)
        pass

    def cancel(self, context: Dict[string, int]):
        pass
