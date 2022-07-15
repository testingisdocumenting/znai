my_var = "a variable"


def my_func(p1, p2):
    """
    text inside my *func* doc
    * list one
    * list two

    Parameters
    ----------
    p1: String
        parameter one description
    p2: String
        parameter two description
    """

    return 2 + 2


class Animal:
    """
    animal top level class doc string

    ```
    code block
    ```
    """

    def says(self):
        """
        animal **talks** `code`
        """
        print("hello")
