# Members Comment Text

Use `include-doxygen-doc` to extract a function description

:include-file: doxygen/src/funcs.h {title: "src/funcs.h"}

    :include-doxygen-doc: utils::nested::my_func

:include-doxygen-doc: utils::nested::my_func

Note: Plugin ignores parameters block and only includes the description text

# Member Args Selection

Use `args` param to pick an overload to use for comments extraction

    :include-doxygen-doc: utils::nested::my_func { title: "My Params", args: "int" }

:include-doxygen-doc: utils::nested::my_func { title: "My Params", args: "int" }

Note: Args are coma and spaces sensitive. Znai will print available args variants in case of mismatch for you to
copy and paste.

# Classes Comment Text

To extract class top level comment, provide full class name

:include-file: doxygen/src/classes.h {title: "src/classes.h"}

    :include-doxygen-doc: utils::second::MyClass

:include-doxygen-doc: utils::second::MyClass

# Extract Parameters

Use `include-doxygen-doc-params` to extract parameters description from doxygen comments

    :include-doxygen-doc-params: utils::nested::my_func { title: "My Params" }

:include-doxygen-doc-params: utils::nested::my_func { title: "My Params" }

Use `small: true` option to make parameters smaller

:include-doxygen-doc-params: utils::nested::my_func { title: "My Params", small: true }

# Extract Parameters By Args

Use `args` to select a specific overload by providing parameters string

    :include-doxygen-doc-params: utils::nested::my_func { title: "My Params", args: "int" }

:include-doxygen-doc-params: utils::nested::my_func { title: "My Params", args: "int" }

# Extract Template Parameters

Pass `type: "template"` parameter to `include-doxygen-doc-params` plugin to extract template parameters

:include-file: doxygen/src/funcs_template.hpp { title: "source.hpp", surroundedBy: "// multi_println" }

    :include-doxygen-doc-params: multi_println { title: "Template parameters", type: "template" }

:include-doxygen-doc-params: multi_println { title: "Template parameters", type: "template" }

# Ignore Template Parameters

Use `doc_ignore` as part of template parameter name to remove it from signature

:include-file: doxygen/src/funcs_template.hpp { title: "source.hpp", surroundedBy: "// ignore_template" }

```markdown {title: "template example"}
:include-doxygen-member: long_template_func {signatureOnly: true}
```

:include-doxygen-member: long_template_func {signatureOnly: true}

# Return Description

Return description is part of parameters list

:include-file: doxygen/src/math.h { title: "math.h", surroundedBy: "// math-add" }

    :include-doxygen-doc-params: math::add

:include-doxygen-doc-params: math::add