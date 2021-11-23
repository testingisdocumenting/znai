# Introduction

Znai parses generated [Doxygen](https://www.doxygen.nl/index.html) XML files to extract and embed
* Comments
* Signatures 
* Parameters

Use the building blocks to mix and match API documentation with visuals and other artifacts.
Or use the built-in plugins to generate the whole blocks of API reference.

# Setup

Specify `index.xml` location inside `<docroot>/doxygen.json`

:include-file: doxygen.json {autoTitle: true}

# Extract Comment Text

## Members Comment Text

Use `include-doxygen-doc` to extract a function description

:include-file: doxygen/src/funcs.h {title: "src/funcs.h"}

    :include-doxygen-doc: utils::nested::my_func

:include-doxygen-doc: utils::nested::my_func

Note: Plugin ignores parameters block and only includes the description text

## Member Args Selection 

Use `args` param to pick an overload to use for comments extraction

    :include-doxygen-doc: utils::nested::my_func { title: "My Params", args: "(int first_param)" }

:include-doxygen-doc: utils::nested::my_func { title: "My Params", args: "(int first_param)" }

Note: Args are coma and spaces sensitive. Znai will print available args variants in case of mismatch for you to
copy and paste.

## Classes Comment Text

To extract class top level comment, provide full class name

:include-file: doxygen/src/classes.h {title: "src/classes.h"}

    :include-doxygen-doc: utils::second::MyClass

:include-doxygen-doc: utils::second::MyClass

# Member Signature, Comment And Parameters

Use `include-doxygen-member` to render member signature, its description and parameters list

    :include-doxygen-member: utils::nested::my_func

:include-doxygen-member: utils::nested::my_func 

```markdown {title: "template example"}
:include-doxygen-member: multi_println
```

:include-doxygen-member: multi_println

# Member Signature Only

Use `signatureOnly: true` parameter to render only member signature

    :include-doxygen-member: utils::second::ThirdClass::bark { signatureOnly: true }

:include-doxygen-member: utils::second::ThirdClass::bark { signatureOnly: true }

# All Matching Signatures

    :include-doxygen-member: utils::nested::their_func { signatureOnly: true, includeAllMatches: true }

:include-doxygen-member: utils::nested::their_func { signatureOnly: true, includeAllMatches: true }


# Specific Member By Args

Use `args` to select a specific overload by providing parameters string

    :include-doxygen-member: utils::nested::their_func { args: "(long param1, bool param3)" }

:include-doxygen-member: utils::nested::their_func { args: "(long param1, bool param3)", disableAnchor: true }

Note: Args are coma and spaces sensitive. Znai will print available args variants in case of mismatch for you to 
copy and paste.

# Extract Parameters

Use `include-doxygen-doc-params` to extract parameters description from doxygen comments

    :include-doxygen-doc-params: utils::nested::my_func { title: "My Params" }

:include-doxygen-doc-params: utils::nested::my_func { title: "My Params" }

Use `small: true` option to make parameters smaller 

:include-doxygen-doc-params: utils::nested::my_func { title: "My Params", small: true }

# Extract Parameters By Args 

Use `args` to select a specific overload by providing parameters string

    :include-doxygen-doc-params: utils::nested::my_func { title: "My Params", args: "(int first_param)" }

:include-doxygen-doc-params: utils::nested::my_func { title: "My Params", args: "(int first_param)" }

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


# Compound

Use `include-doxygen-compound` to render definition compound such as class 

    :include-doxygen-compound: utils::second::AnotherClass

:include-doxygen-compound: utils::second::AnotherClass 


# Cross-Reference

Znai uses doxygen cross-reference to link definitions together. 
Class above references `MyClass` in its parameters. After you include definition of that class,
`MyClass` reference above becomes a link.

    :include-doxygen-compound: utils::second::MyClass

:include-doxygen-compound: utils::second::MyClass 
