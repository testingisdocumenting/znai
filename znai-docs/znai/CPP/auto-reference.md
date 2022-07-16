# Member Signature, Comment And Parameters

Use `include-doxygen-member` to render member signature, its description and parameters list

:include-file: doxygen/src/funcs.h {title: "src/funcs.h"}

    :include-doxygen-member: utils::nested::my_func

:include-doxygen-member: utils::nested::my_func

```markdown {title: "template example"}
:include-doxygen-member: multi_println
```

:include-file: doxygen/src/funcs_template.hpp { title: "source.hpp", surroundedBy: "// multi_println" }

:include-doxygen-member: multi_println

# Member Signature Only

Use `signatureOnly: true` parameter to render only member signature

:include-file: doxygen/src/classes.h {title: "src/classes.h"}

    :include-doxygen-member: utils::second::ThirdClass::bark { signatureOnly: true }

:include-doxygen-member: utils::second::ThirdClass::bark { signatureOnly: true }

# All Matching Signatures

:include-file: doxygen/src/funcs_two.h {title: "src/funcs.h"}

    :include-doxygen-member: utils::nested::their_func { signatureOnly: true, includeAllMatches: true }

:include-doxygen-member: utils::nested::their_func { signatureOnly: true, includeAllMatches: true }


# Specific Member By Args

Use `args` to select a specific overload by providing parameters string

    :include-doxygen-member: utils::nested::their_func { args: "long, bool" }

:include-doxygen-member: utils::nested::their_func { args: "long, bool", disableAnchor: true }

Note: Args are coma and spaces sensitive. Znai will print available args variants in case of mismatch for you to
copy and paste.

# Compound

Use `include-doxygen-compound` to render a definition of a class/struct

    :include-doxygen-compound: utils::second::AnotherClass

:include-doxygen-compound: utils::second::AnotherClass


# Cross-Reference

Znai uses doxygen cross-reference to link definitions together.
Class above references `MyClass` in its parameters. After you include definition of that class,
`MyClass` reference above becomes a link.

    :include-doxygen-compound: utils::second::MyClass

:include-doxygen-compound: utils::second::MyClass 
