# Introduction

Znai parses generated [Doxygen](https://www.doxygen.nl/index.html) XML files to extract and embed
* Comments
* Signatures (coming)
* Parameters (coming)

# Setup

Specify `index.xml` location inside `<docroot>/doxygen.json`

:include-file: doxygen.json {autoTitle: true}

# Doc Comments

Use `include-doxygen-doc` to extract a function description

:include-file: doxygen/src/funcs.h {title: "src/funcs.h"}

    :include-doxygen-doc: utils::nested::my_func

:include-doxygen-doc: utils::nested::my_func

Note: Plugin ignores parameters block and only includes the description text
