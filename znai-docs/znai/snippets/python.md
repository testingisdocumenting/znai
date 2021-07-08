# Function Content

Note: Function Content support requires running znai in an environemnt with Python 3.8 or later.

Use `include-python` plugin to extract a function content.

:include-file: python/example.py {title: "example.py"}

    :include-python: python/example.py {entry: "Animal.says"}

:include-python: python/example.py {entry: "Animal.says"}

Use `bodyOnly` to include only the content of a function without its signature. 

    :include-python: python/example.py {entry: "Animal.says", bodyOnly: true}

:include-python: python/example.py {entry: "Animal.says", bodyOnly: true}


# Doc String

Use `include-python-doc` plugin to embed py doc string with markdown into documentation

:include-file: python/example.py {title: "example.py"}

    :include-python-doc: python/example.py {entry: "my_func"}

:include-python-doc: python/example.py {entry: "my_func"}

    :include-python-doc: python/example.py {entry: "Animal"}

:include-python-doc: python/example.py {entry: "Animal"}

    :include-python-doc: python/example.py {entry: "Animal.says"}
    
:include-python-doc: python/example.py {entry: "Animal.says"}
