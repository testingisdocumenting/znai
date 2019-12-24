# Local References

You can turn parts of a code into links to internal or external pages. 

To do that, define references in a csv file. Two columns format: `expression, link`.

:include-file: references/references-demo.csv {title: "references/references-demo.csv"}

    :include-file: file-name.js {referencesPath: "references/references-demo.csv"}
    
:include-file: file-name.js {referencesPath: "references/references-demo.csv"}

# Global References

Add references to `references.csv` if you want all your code snippets to use the same references.

:include-file: references.csv {title: "references.csv"}

:include-file: global-references.js {title: "API example"}
