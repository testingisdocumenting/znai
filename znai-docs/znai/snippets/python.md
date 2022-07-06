# Content

Note: Function Content support requires running znai in an environment with Python 3.8 or later.

All the examples below are from the following example Python file:

:include-file: python/example.py {title: "example.py"}

## Full Content

Use `include-python` plugin to extract function, class or global variable content.

### Class

To show the full class definition:

    :include-python: python/example.py {entry: "Animal"}

:include-python: python/example.py {entry: "Animal"}

### Function

To show the full contents of a function:

    :include-python: python/example.py {entry: "Animal.says"}

:include-python: python/example.py {entry: "Animal.says"}

This also works for global functions:

    :include-python: python/example.py {entry: "my_func"}

:include-python: python/example.py {entry: "my_func"}

### Class

To show the full contents of a class:

    :include-python: python/example.py {entry: "Animal"}

:include-python: python/example.py {entry: "Animal"}

### Variable

To show a variable's definition and assignment:

    :include-python: python/example.py {entry: "my_var"}

:include-python: python/example.py {entry: "my_var"}

## Body Only

Use `bodyOnly` to include just the "body" part of the content.  The specifics differ based on what is being included.

### Function

To show a function's body, without signature or doc string:

    :include-python: python/example.py {entry: "Animal.says", bodyOnly: true}

:include-python: python/example.py {entry: "Animal.says", bodyOnly: true}

This also works for global functions:

    :include-python: python/example.py {entry: "my_func", bodyOnly: true}

:include-python: python/example.py {entry: "my_func", bodyOnly: true}

### Class

To show the contents of a class without the class declaration or doc string:

    :include-python: python/example.py {entry: "Animal", bodyOnly: true}

:include-python: python/example.py {entry: "Animal", bodyOnly: true}

### Variable

To show only a variable's value:

    :include-python: python/example.py {entry: "my_var", bodyOnly: true}

:include-python: python/example.py {entry: "my_var", bodyOnly: true}

# Doc String

Use `include-python-doc` plugin to embed py doc string with markdown into documentation

:include-file: python/example.py {title: "example.py"}

    :include-python-doc: python/example.py {entry: "my_func"}

:include-python-doc: python/example.py {entry: "my_func"}

Note: Plugin ignores parameters block and only includes the description text. Check next section to see how to include
parameters

    :include-python-doc: python/example.py {entry: "Animal"}

:include-python-doc: python/example.py {entry: "Animal"}

    :include-python-doc: python/example.py {entry: "Animal.says"}
    
:include-python-doc: python/example.py {entry: "Animal.says"}

# Doc Parameters

Use `include-python-doc-params` plugin to render parameters extracted from pydoc text

:include-file: python/pydoc-params.py {autoTitle: true}

    :include-python-doc-params: python/pydoc-params.py {entry: "my_func", title: "my_func params"}

:include-python-doc-params: python/pydoc-params.py {entry: "my_func", title: "my_func params"}

Note: read more about [API parameters](snippets/api-parameters) for additional properties   

# Type Hints

If you define [Type Hints](https://docs.python.org/3/library/typing.html), you can omit types from PyDoc text and Znai will take the types from the signature

:include-file: python/pydoc-params-type-hints.py {autoTitle: true}

    :include-python-doc-params: python/pydoc-params-type-hints.py {entry: "my_func", title: "my_func params"}

:include-python-doc-params: python/pydoc-params-type-hints.py {entry: "my_func", title: "my_func params"}

# Type Definition

:include-python-class: fin.money.Money