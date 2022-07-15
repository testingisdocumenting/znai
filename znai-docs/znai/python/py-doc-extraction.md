# Doc String

Use `include-python-doc` plugin to embed py doc string with markdown into documentation.
Znai uses [Pandas style doc string](https://pandas.pydata.org/docs/development/contributing_docstring.html) to parse description and parameters.

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

    :include-python-doc-params: python/pydoc-params.py {entry: "my_func", title: "result and parameters"}

:include-python-doc-params: python/pydoc-params.py {entry: "my_func", title: "result and parameters"}

Note: read more about [API parameters](snippets/api-parameters) for additional properties

# Type Hints

If you define [Type Hints](https://docs.python.org/3/library/typing.html), you can omit types from PyDoc text and Znai will take the types from the signature

:include-file: python/pydoc-params-type-hints.py {autoTitle: true}

    :include-python-doc-params: python/pydoc-params-type-hints.py {entry: "my_func", title: "result and parameters"}

:include-python-doc-params: python/pydoc-params-type-hints.py {entry: "my_func", title: "result and parameters"}
