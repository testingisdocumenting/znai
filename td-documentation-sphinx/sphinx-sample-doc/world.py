class HelloWorld:
    """Simple hello world class"""

    def __init__(self):
        """constructor that is doing something useful"""
        self.data = []

    def sayHi(self, name, title):
        """says hi

        :param name: name of a person to greet
        :param title: title of a person to greet
        """
        print("hi %s %s" % (title, name))

    def sayBye(self, name, title):
        """says bye

        :param name: name of a person to greet
        :param title: title of a person to greet
        """
        print("bye %s %s" % (title, name))


class CarMarket:
    """Car market"""
    def __init__(self):
        pass

    def explore(self):
        print("explore")