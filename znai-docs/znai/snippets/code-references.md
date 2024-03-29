# Local References

You can turn parts of a code snippet into links to internal or external pages. 

To do that, define references in a CSV file, using a two column format: `expression, link`.

:include-file: references/references-demo.csv {autoTitle: true}

    :include-file: file-name.js {referencesPath: "references/references-demo.csv"}
    
:include-file: file-name.js {referencesPath: "references/references-demo.csv"}

JSON format is also supported

:include-json: references/references-demo.json {autoTitle: true}

# Page Defaults

Use plugin [Page Local Defaults](plugins/default-parameters#page-local-defaults) to set references file that is relevant to the page at hand.

# Global References

Add references to `references.csv` or `references.json` if you want all your code snippets to use the same references.

:include-file: references.json {autoTitle: true}

:include-file: global-references.js {title: "API example"}

# Inlined Code

[Inlined code](snippets/inlined-code-snippets) is automatically converted into a link if its content matches one of the
global references entries:

:include-file: code-reference-inlined-code-example.md {title: "Example"}

:include-markdown: code-reference-inlined-code-example.md
