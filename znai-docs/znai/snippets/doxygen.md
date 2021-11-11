# Introduction

Znai parses generated [Doxygen](https://www.doxygen.nl/index.html) XML files to extract and embed
* Comments
* Signatures (coming)
* Parameters (coming)

# Setup

Specify `index.xml` location inside `<docroot>/doxygen.json`

:include-file: doxygen.json {autoTitle: true}

# Extract Comment Text

## Functions And Methods

Use `include-doxygen-doc` to extract a function description

:include-file: doxygen/src/funcs.h {title: "src/funcs.h"}

    :include-doxygen-doc: utils::nested::my_func

:include-doxygen-doc: utils::nested::my_func

Note: Plugin ignores parameters block and only includes the description text

## Classes

To extract class top level comment, provide full class name

:include-file: doxygen/src/classes.h {title: "src/classes.h"}

    :include-doxygen-doc: utils::second::MyClass

:include-doxygen-doc: utils::second::MyClass

# Member Signature

Use `include-doxygen-member` to render member signature

    :include-doxygen-member: utils::nested::my_func

:include-doxygen-member: utils::nested::my_func 

# Extract Comment Parameters

Use `include-doxygen-doc-params` to extract parameters description from doxygen comments

    :include-doxygen-doc-params: utils::nested::my_func { title: "My Params" }

:include-doxygen-doc-params: utils::nested::my_func { title: "My Params" }

