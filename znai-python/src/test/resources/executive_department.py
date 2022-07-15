from department import Worker as Wo
from fin.money import Money


class WorkerCTO(Wo):
    """
    CTO worker with all the qualities and attributes required for a successful business
    """
    def __init__(self, name: str, salary: Money, goal: str):
        """
        Create a new instance of CTO with provided name, salary and the goal

        Parameters
        ----------
        name:
          CTO name
        salary:
          CTO salary
        goal:
          top priority goal to achieve
        """
        Wo.__init__(self, name=name, salary=salary)
        self._goal = goal

    def work_hard(self, level: int):
        """
        work as hard as possible to observe

        Parameters
        ----------
        level:
          effort level from 0 to 100
        """
        pass

    @property
    def goal(self) -> str:
        """
        CTO's top priority goal to achieve
        """
        return self._goal

    @goal.setter
    def goal(self, new_goal: str):
        self._goal = new_goal


