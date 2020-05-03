# Paths

Use the `include-json` plugin to bring attention to a certain place in a `JSON` file. 

    :include-json: book-store.json {paths: ["root.store.book[0].category", "root.store.book[2].category"]}

Comma-separated paths specified inside `paths` will be highlighted.

:include-json: book-store.json {paths: ["root.store.book[0].category", "root.store.book[2].category"]}

# Paths From File

Use `pathsFile` to specify a file to read paths from.

:include-json: book-store-paths.json {title: "book-store-paths.json"} 

    :include-json: book-store.json {pathsFile: "book-store-paths.json"}

# Json Subparts

To include only a portion of your document 
pass [Json Path](https://github.com/json-path/JsonPath) as `include` property:

    :include-json: book-store.json {include: "$..book[0,1]"}

:include-json: book-store.json {include: "$..book[0,1]"}

# Title

To specify a title use the `title` property:

    :include-json: book-store.json {include: "$..book[0,1]", title: "Books"}
    
:include-json: book-store.json {include: "$..book[0,1]", title: "Books"}

# Read More

Use `readMore` to show only first lines of `JSON`. **Optional** `readMoreVisibleLines` can be specified to set 
the initial number of lines to display 

    :include-json: book-store.json {readMore: true, readMoreVisibleLines: 5}

:include-json: book-store.json {readMore: true, readMoreVisibleLines: 5}

# Hidden Parts

To hide sub-parts of your `JSON` use `collapsedPaths` property.

    :include-json: book-store.json {collapsedPaths: ['root.store.book']}
    
:include-json: book-store.json {collapsedPaths: ['root.store.book']}

# Highlights

To highlight a specific `JSON` value using `paths`, in (similar fashion to [regular code snippets](snippets/external-code-snippets#highlights)), you can highlight 
a line by text matching or by providing a line index.

    :include-json: book-store.json {highlight: ["category", 2]}
    
:include-json: book-store.json {highlight: ["category", 2]}

# Code References

You can turn parts of `JSON` into links to internal or external pages. 


:include-json: book-store.json {include: "$..book[0,1]", title: "Books", referencesPath: "references/json-references-demo.csv"}

# Test Results

Consider leveraging testing frameworks to extract `JSON` samples from your endpoints. 
Information about what assertions were made can be used to highlight points of interest.
