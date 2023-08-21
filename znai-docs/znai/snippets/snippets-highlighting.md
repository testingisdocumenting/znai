# Line By Text

Use the `highlight` option to bring readers attention to the important lines.

    :include-file: file-name.js {highlight: "export"}

:include-file: file-name.js {highlight: "export"}

# Line By Index

It is recommended to pass a substring, but you can pass a line idx (starts from 0).
Additionally, you can combine two approaches and pass a list of things to highlight.

    :include-file: file-name.js {highlight: ["export", 1]}

:include-file: file-name.js {highlight: ["export", 1]}

Note: Order of lines to highlight is reflected during presentation mode

# Text From File

Use the `highlightPath` option to highlight lines specified in a separate file.

    :include-file: file-name.js {highlightPath: "lines-to-highlight.txt"}

:include-file: file-name.js {highlightPath: "lines-to-highlight.txt"}

:include-file: lines-to-highlight.txt {title: "lines-to-highlight.txt"}

# Region

Use the `highlightRegion` to highlight large portions of the code as one highlight block:

    :include-file: file-name.js {highlightRegion: {start: "constructor", end: "}"}}

:include-file: file-name.js {highlightRegion: {start: "constructor", end: "}"}}

# Region Using Scope

Use the `highlightRegion.scope` to highlight region using scope like `{}`.
Unlike example above, highlight will not stop at first closing `}`:

    :include-file: with-nested-if.js {highlightRegion: {start: "userId ===", scope: "{}"}}

:include-file: with-nested-if.js {highlightRegion: {start: "userId ===", scope: "{}"}}

# Multiple Regions

Use list to specify multiple regions to highlight:

```markdown
:include-file: with-multiple-if.js {highlightRegion: [
  {start: "canRead", scope: "{}"},
  {start: "canWrite", scope: "{}"}]
}
```

:include-file: with-multiple-if.js {highlightRegion: [
{start: "canRead", scope: "{}"},
{start: "canWrite", scope: "{}"}]
}
