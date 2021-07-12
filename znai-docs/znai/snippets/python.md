# Content

Note: Function Content support requires running znai in an environemnt with Python 3.8 or later.

Use `include-python` plugin to extract functuion, class or global variable content.

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

# Doc Parameters

Use `include-python-doc-params` plugin to render parameters extracted from pydoc text

:include-file: python/pydoc-params.py {title: "pydoc-params.py"}

    :include-python-doc-params: python/pydoc-params.py {entry: "my_func", title: "my_func params"}

:include-python-doc-params: python/pydoc-params.py {entry: "my_func", title: "my_func params"}

Note: read more about [API parameters](snippets/api-parameters) for additional properties   
