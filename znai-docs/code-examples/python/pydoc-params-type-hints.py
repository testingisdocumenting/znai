import fin


def my_func(label: str, price: fin.money.Money) -> str:
    """
    text inside my *func* doc
    * list one
    * list two

    Parameters
    --------------
    label :
      label to use to *render* item in the store

    price :
      price associated with the **item**

    Returns
    -------
      status of the operation

      `OK` for good
    """

    return "OK"
