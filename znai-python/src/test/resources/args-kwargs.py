from fin.money import Money


def position_only_with_default(message, name: str = "no-name", /, title: str = ""):
    print(message + " " + title + " " + name)


def default_kwarg_values(message, name: str = "Default", *prices, label: str = "Hello", price: int = 10,
                         money=Money(100),
                         **opts):
    print(message + " " + label + " " + name)
    print("*prices[0]=" + prices[0])


def my_sum(label="hello", *prices: Money):
    result = 0

    for x in prices:
        result += x

    return result


def default_values_with_args(message, label: str = "Hello", name="Default", price: int = 10, money=Money(
    100), **opts):
    print(message + " " + label + " " + name + " " + str(money))


position_only_with_default("hello", "Alice", title="lady")
position_only_with_default("hello", title="lady")
