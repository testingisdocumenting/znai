# Content

Note: Function Content support requires running znai in an environment with Python 3.8 or later.

All the examples below are from the following example Python file:

:include-file: python/example.py {autoTitle: true}

Use `include-python` plugin to extract function, class or global variable content.

# Function

To show the full contents of a function:

    :include-python: python/example.py {entry: "Animal.says"}

:include-python: python/example.py {entry: "Animal.says"}

This also works for global functions:

    :include-python: python/example.py {entry: "my_func"}

:include-python: python/example.py {entry: "my_func"}

To show a function's body, without signature or doc string:

    :include-python: python/example.py {entry: "Animal.says", bodyOnly: true}

:include-python: python/example.py {entry: "Animal.says", bodyOnly: true}

This also works for global functions:

    :include-python: python/example.py {entry: "my_func", bodyOnly: true}

:include-python: python/example.py {entry: "my_func", bodyOnly: true}

# Title

Use the `title` property to specify an extracted snippet title.

    :include-python: python/example.py {entry: "Animal.says", bodyOnly: true, title: "extracted snippet"}

:include-python: python/example.py {entry: "Animal.says", bodyOnly: true, title: "extracted snippet"}

# Anchor

Use `anchorId` to make a code snippet linkable. Hover mouse over title to see a clickable anchor.

    :include-python: python/example.py {
      entry: "Animal.says", 
      bodyOnly: true,
      title: "extracted snippet",
      anchorId: "my-extracted-snippet"
    }

:include-python: python/example.py {
  entry: "Animal.says", 
  bodyOnly: true,
  title: "extracted snippet",
  anchorId: "my-extracted-snippet"
}


# Variable

To show a variable's definition and assignment:

    :include-python: python/example.py {entry: "my_var"}

:include-python: python/example.py {entry: "my_var"}

To show only a variable's value:

    :include-python: python/example.py {entry: "my_var", bodyOnly: true}

:include-python: python/example.py {entry: "my_var", bodyOnly: true}

# Class

To show the full class definition:

    :include-python: python/example.py {entry: "Animal"}

:include-python: python/example.py {entry: "Animal"}

To show the contents of a class without the class declaration or doc string:

    :include-python: python/example.py {entry: "Animal", bodyOnly: true}

:include-python: python/example.py {entry: "Animal", bodyOnly: true}

