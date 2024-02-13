---
identifier: {validationPath: "org/testingisdocumenting/znai/text/TextContentExtractor.java"}
---

# Surrounded By

Use `:identifier: surroundedBy` to extract code snippet surrounded by a marker

:include-file: python-examples.py {title: "file with examples"}

    :include-file: python-examples.py {
        title: "extracted example with surroundedBy",
        surroundedBy: "# example-cancel-trade"}

:include-file: python-examples.py {
  title: "extracted result",
  surroundedBy: "# example-cancel-trade"}

Use `:identifier: surroundedByKeep` to keep marker lines

:include-file: custom.dsl {autoTitle: true}

```markdown {highlight: "surroundedByKeep"}
:include-file: custom.dsl {
  title: "keep marker lines",
  surroundedBy: "block-A",
  surroundedByKeep: true
}
```

:include-file: custom.dsl {
  title: "keep marker lines",
  surroundedBy: "block-A",
  surroundedByKeep: true
}

# Multiple Surrounded By 

Pass a list to `:identifier: surroundedBy` to extract multiple blocks

    :include-file: python-examples.py {
      title: "extracted example",
      surroundedBy: ["# example-import-block", "# example-cancel-trade"]}

:include-file: python-examples.py {
  title: "extracted result",
  surroundedBy: ["# example-import-block", "# example-cancel-trade"]}

Use `:identifier: surroundedBySeparator` to select separator(s) between blocks
  
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

Note: `:identifier: surroundedBySeparator` can be either single value or a list. Plugin will use a different separator for each block. Use `null` to have an empty line as a separator.  

# Surrounded By Scope

Use `:identifier: surroundedByScope` to extract text using scopes like `{}`, `[]`, etc:

:include-file: MyClass.java {autoTitle: true}

```markdown
:include-file: MyClass.java {surroundedByScope: {start: "if (condition)", scope: "{}"}}
```

:include-file: MyClass.java {title: "extraction result", surroundedByScope: {start: "if (condition)", scope: "{}"}}

# Surrounded By Multi-chars scope

Pass comma separated multi char region definition to `:identifier: surroundedByScope` to handle scopes defined with keywords:

:include-file: ocaml/model.mli {autoTitle: true}

```markdown
:include-file: ocaml/model.mli {surroundedByScope: {start: "module ModelA", scope: "sig,end"}}
```

:include-file: ocaml/model.mli {title: "extraction result", surroundedByScope: {start: "module ModelA", scope: "sig,end"}}

# Replace

Use `:identifier: replace` to replace content of the resulting snippet

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

Pass a list of lists to `:identifier: replace` for multiple replaces

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

Use `:identifier: startLine`, `:identifier: endLine` to extract specific content by using marker lines.

:include-file: python-examples.py {title: "file with examples"}

    :include-file: python-examples.py {startLine: "example: book trade", endLine: "example-end"}

:include-file: python-examples.py {title: "extracted example", startLine: "example: book trade", endLine: "example-end"}

Note: Lines match doesn't have to be exact, `contains` is used.

By default `:identifier: startLine` and `:identifier: endLine` are included in the rendered result. Use `:identifier: excludeStartEnd` to remove markers.

    :include-file: python-examples.py { 
        startLine: "example: book trade",
        endLine: "example-end",
        excludeStartEnd: true }

:include-file: python-examples.py {
  title: "extracted result",
  startLine: "example: book trade",
  endLine: "example-end",
  excludeStartEnd: true }

To exclude start or end line only use `:identifier: excludeStart` and `:identifier: excludeEnd`

# Start/End Multiple Lines

Pass multiple values to `startLine` and `endLine` to specify more precisely a block of code to extract.

As example, let's extract a second `match` block from the code snippets below without adding extra markers:

:include-file: ocaml/seasons.ml {autoTitle: true}

if we use `startLine: "match season"`, we can't guarantee what block will it pick, and it will depend on
functions order.

To hit the exact one use multiple start lines. Znai will attempt to find a combination of start lines with the smallest distance between them:

```
:include-file: ocaml/seasons.ml {startLine: ["let describe_season", "match season"], startLineKeepLast: true}
```

:include-file: ocaml/seasons.ml {title: "result", startLine: ["let describe_season", "match season"], startLineKeepLast: true}

Note: Use `endLine` and `endLineKeepFirst` to limit lines from the bottom. `excludeStart` removes all the lines between specified start lines, including the lines. 
`excludeEnd` removes all the lines between specified end lines, including the lines.


# Include Contains

Use `:identifier: include` to include only lines containing specified text(s).

    :include-file: python-examples.py { include: "import " }
    or
    :include-file: python-examples.py { include: ["import "] }

:include-file: python-examples.py { include: ["import "], title: "include lines containing specified lines" }

# Exclude Contains

Use `:identifier: exclude` to exclude lines containing specified text(s).

    :include-file: python-examples.py { exclude: "# example" }
    or
    :include-file: python-examples.py { exclude: ["# example"] }

:include-file: python-examples.py { exclude: ["# example"], title: "exclude by regexp result" }


# Include Regexp

Use `:identifier: includeRegexp` to include only lines matching regexp(s).

    :include-file: python-examples.py { includeRegexp: "market.*_trade" }
    or
    :include-file: python-examples.py { includeRegexp: ["market.*_trade"] }

:include-file: python-examples.py { includeRegexp: ["market.*_trade"], title: "include by regexp result" }

# Exclude Regexp

Use `:identifier: excludeRegexp` to exclude lines matching regexp(s).

    :include-file: python-examples.py { excludeRegexp: ["if .* ==", "\\s+main", "# example", "^\\s*$"] }

:include-file: python-examples.py { excludeRegexp: ["if .* ==", "\\s+main", "# example", "^\\s*$"], title: "exclude by regexp result" }
