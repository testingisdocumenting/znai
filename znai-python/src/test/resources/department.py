from fin.money import Money


class Person:
    """
    Person, the root of all
    """
    def __init__(self, name):
        self._name = name
        self._desire = ""

    @property
    def name(self) -> str:
        """
        Person name, you'd better use it when talking to them
        """
        return self._name

    @property
    def desire(self) -> str:
        """
        Person desire, can change over time
        """
        return self._desire

    @desire.setter
    def desire(self, new_desire: str):
        self._desire = new_desire

    def talk(self):
        """
        person talks
        """
        print(f"{self.name} talks")


class Worker(Person):
    """
    Works and improves the world
    """
    def __init__(self, name: str, salary: Money):
        Person.__init__(self, name)
        self._salary = salary

    def work(self):
        """
        work to the best ability
        """
        pass

    @property
    def salary(self) -> Money:
        """
        Annual non-yet-taxed salary
        """
        return self._salary


