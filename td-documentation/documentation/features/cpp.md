# Comments

You can re-use comments that are inside your functions and refer that inside documentation.
Mark a comment with `@mdoc` and use `:include-comments:` plugin.

:include-file: comments.cpp

Specify a file and an entry point to extract comments from.

    :include-cpp-comments: comments.cpp {entry: "my_func"}

Result will be a text rendered as if it was typed inside markup file. See text below.


:include-cpp-comments: comments.cpp {entry: "my_func"}
