#  Copyright 2021 znai maintainers
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

def a_method():
    """
    This method does stuff
    :return: nothing
    """
    pass


class AClass:
    """
    This class does things.
    """
    def foo(self):
        pass


def func_no_docs():
    a = 2
    b = 3
    return a + b


def my_func(a, b,
            c, d):
    """
    text inside my *func* doc
    """

    e = a + b
    f = c + d

    return e + f


def another_func():
    """
    more diff text
    """
    a = 2
    b = 3
    return a + b


class Animal:
    """
    animal top level class doc string
    """

    def says(self, message, volume):
        """
        animal talks

        Parameters
        ___________
        message : string

          message to say

        volume : int (default 0)

          how loud it is
        """

        print("hello")
