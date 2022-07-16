# Description Extraction

In addition to [snippets manipulation](snippets/snippets-manipulation) that is applicable to any language,
Znai can extract description of methods and convert parameters into [API Parameters](snippets/api-parameters)

    :include-doxygen-doc-params: utils::nested::my_func { title: "My Params" }

:include-doxygen-doc-params: utils::nested::my_func { title: "My Params" }

Head over to [CPP Description Extraction](CPP/description-extraction) to learn more

# Auto Reference

Znai provides plugins to automatically create reference documentation for methods and classes.

    :include-doxygen-member: multi_println

:include-doxygen-member: multi_println

:include-file: doxygen/src/funcs_template.hpp { title: "source.hpp", surroundedBy: "// multi_println" }

Head over to [CPP Auto Reference](CPP/auto-reference) to learn more
