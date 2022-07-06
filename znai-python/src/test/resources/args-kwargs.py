import fin.money
from fin.money import Money


def my_sum(label, *prices: fin.money.Money):
    result = 0

    for x in prices:
        result += x

    return result


def default_arg_value(message, label="Hello", name="Default", price=10, money=Money(100), **opts):
    print(message + " " + label + " " + name)


def default_kwarg_value(message, *prices, label="Hello", name="Default", price=10, money=Money(100), **opts):
    print(message + " " + label + " " + name)
    print("*prices[0]=" + prices[0])


default_arg_value("message text", "hello-hello", price=23, money=Money(200), something="else")
