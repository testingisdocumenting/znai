# Content Extraction

In addition to [snippets manipulation](snippets/snippets-manipulation) that is applicable to any language,
Znai can extract content of methods.

    :include-python: python/example.py {entry: "Animal.says", bodyOnly: true}

:include-python: python/example.py {entry: "Animal.says", bodyOnly: true}

:include-file: python/example.py {autoTitle: true, startLine: "class Animal"}

Head over to [Python Content Extraction](python/content-extraction) to learn more

# Description Extraction

Znai provides plugin to extract PyDoc content. Use it to extract high level description and merge it with the rest of the documentation.
Convert method parameters into [API Parameters](snippets/api-parameters)

    :include-python-doc-params: python/pydoc-params.py {entry: "my_func", title: "result and parameters"}

:include-python-doc-params: python/pydoc-params.py {entry: "my_func", title: "result and parameters"}

:include-file: python/pydoc-params.py {autoTitle: true}

Head over to [Python Description Extraction](python/description-extraction) to learn more

# Auto Reference

Znai provides plugins to automatically create reference documentation for methods and classes.

    :include-python-method: fin.money.render_money

:include-python-method: fin.money.render_money

:include-file: fin/money.py {
  autoTitle: true,
  startLine: "render_money"
}

Head over to [Python Auto Reference](python/auto-reference) to learn more
