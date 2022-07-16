# Comments

You can reuse comments that are inside your functions and refer to them inside your documentation.
Mark a comment with `@znai` and use `:include-cpp-comments:` plugin.

:include-file: comments.cpp

Specify a file and an entry point to extract comments from.

    :include-cpp-comments: comments.cpp {entry: "my_func"}

Result will be a text rendered as if it was typed inside markup file. See the text immediately below:

:include-cpp-comments: comments.cpp {entry: "my_func"}
