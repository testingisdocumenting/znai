# Surrounded By

Use `surroundedBy` to extract code snippet surrounded by a marker

:include-file: python-examples.py {title: "file with examples"}

    :include-file: python-examples.py {
        title: "extracted example with surroundedBy",
        surroundedBy: "# example-cancel-trade"}

:include-file: python-examples.py {
  title: "extracted result",
  surroundedBy: "# example-cancel-trade"}

# Multiple Surrounded By 

Pass a list to `surroundedBy` to extract multiple blocks

    :include-file: python-examples.py {
      title: "extracted example",
      surroundedBy: ["# example-import-block", "# example-cancel-trade"]}

:include-file: python-examples.py {
  title: "extracted result",
  surroundedBy: ["# example-import-block", "# example-cancel-trade"]}

Use `surroundedBySeparator` to select separator(s) between blocks
  
    :include-file: python-examples.py {
      title: "extracted example",
      surroundedBy: ["# example-import-block", "# example-cancel-trade"],
      surroundedBySeparator: ["..."]
    }

:include-file: python-examples.py {
  title: "extracted result",
  surroundedBy: ["# example-import-block", "# example-cancel-trade"],
  surroundedBySeparator: ["..."]
}

Note: `surroundedBySeparator` can be either single value or a list. Plugin will use a different separator for each block. Use `null` to have an empty line as a separator.  

# Replace

Use `replace` to replace content of the resulting snippet

```markdown {highlight: "replace"}
:include-file: python-examples.py {
  surroundedBy: "# example-cancel-trade",
  replace: ['id', '"trade_id"'] }
```

:include-file: python-examples.py {
  surroundedBy: "# example-cancel-trade",
  replace: ['id', '"trade_id"'],
  title: "replace result"
}

Pass a list of lists to `replace` for multiple replaces

```markdown {highlight: "replace"}
:include-file: python-examples.py {
  surroundedBy: "# example-cancel-trade",
  replace: [['id', '"trade_id"'], ["market", "api"]]}
```

:include-file: python-examples.py {
  title: "replace multiple result",
  surroundedBy: "# example-cancel-trade",
  replace: [['id', '"trade_id"'], ["market", "api"]]}

# Replace Regexp Groups

Use `$1` regexp capture groups to create derived content

:include-file: replace-all-group.txt {autoTitle: true}

    :include-file: replace-all-group.txt {replace: ["(\\w+)(\\d+)", "$2-$1"]}

:include-file: replace-all-group.txt {
  title: "replace regexp group result",
  replace: ["(\\w+)(\\d+)", "$2-$1"]}


# Start/End Line

Use `startLine`, `endLine` to extract specific content by using marker lines.

:include-file: python-examples.py {title: "file with examples"}

    :include-file: python-examples.py {startLine: "example: book trade", endLine: "example-end"}

:include-file: python-examples.py {title: "extracted example", startLine: "example: book trade", endLine: "example-end"}

Note: Lines match doesn't have to be exact, `contains` is used.

By default `startLine` and `endLine` are included in the rendered result. Use `excludeStartEnd: true` to remove markers.

    :include-file: python-examples.py { 
        startLine: "example: book trade",
        endLine: "example-end",
        excludeStartEnd: true }

:include-file: python-examples.py {
  title: "extracted result",
  startLine: "example: book trade",
  endLine: "example-end",
  excludeStartEnd: true }

# Include Regexp

Use `includeRegexp` to include only lines matching regexp(s).

    :include-file: python-examples.py {includeRegexp: "import "}
    :include-file: python-examples.py {includeRegexp: ["import "]}

:include-file: python-examples.py { includeRegexp: ["import "], title: "include by regexp result" }

# Exclude Regexp

Use `excludeRegexp` to exclude lines matching regexp(s).

    :include-file: python-examples.py {excludeRegexp: "# example"}
    :include-file: python-examples.py {excludeRegexp: ["# example"]}

:include-file: python-examples.py { excludeRegexp: ["# example"], title: "exclude by regexp result" }

