# Surrounded By

Use `surroundedBy` and a marker to extract code snippet surrounded by a marker

:include-file: python-examples.py {title: "file with examples"}

    :include-file: python-examples.py {
        title: "extracted example with surroundedBy",
        surroundedBy: "# example-cancel-trade"}

:include-file: python-examples.py {
  title: "extracted example",
  surroundedBy: "# example-cancel-trade"}


# Start/End Line

Use `startLine`, `endLine` to extract specific content by using marker lines.

    :include-file: python-examples.py {startLine: "example: book trade", endLine: "example-end"}

:include-file: python-examples.py {title: "extracted example", startLine: "example: book trade", endLine: "example-end"}

Note: Lines match doesn't have to be exact, `contains` is used.

By default `startLine` and `endLine` are included in the rendered result. Use `excludeStartEnd: true` to remove markers.

    :include-file: python-examples.py { 
        startLine: "example: book trade",
        endLine: "example-end",
        excludeStartEnd: true }

:include-file: python-examples.py {
  title: "extracted example",
  startLine: "example: book trade",
  endLine: "example-end",
  excludeStartEnd: true }

# Include Regexp

Use `includeRegexp` to include only lines matching regexp(s).

    :include-file: python-examples.py {includeRegexp: "import"}
    :include-file: python-examples.py {includeRegexp: ["import"]}

:include-file: python-examples.py { includeRegexp: ["import"], title: "include by regexp" }

# Exclude Regexp

Use `excludeRegexp` to exclude lines matching regexp(s).

    :include-file: python-examples.py {excludeRegexp: "# example"}
    :include-file: python-examples.py {excludeRegexp: ["# example"]}

:include-file: python-examples.py { excludeRegexp: ["# example"], title: "exclude by regexp" }

