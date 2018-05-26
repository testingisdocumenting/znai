# Paths

Use `include-json` plugin to bring attention to a certain place in a json file. 

    :include-json: book-store.json {paths: ["root.store.book[0].category", "root.store.book[2].category"]}

To highlight individual elements pass a list or single value via `paths` property.

:include-json: book-store.json {paths: ["root.store.book[0].category", "root.store.book[2].category"]}

# Paths From File

Use `pathsFile` to specify a file to read paths from

:include-json: book-store-paths.json {title: "book-store-paths.json"} 

    :include-json: book-store.json {pathsFile: "book-store-paths.json"}

# Json Subparts

Pass [Json Path](https://github.com/json-path/JsonPath) as `include` property 
to include only a portion of your document

    :include-json: book-store.json {include: "$..book[0,1]"}

:include-json: book-store.json {include: "$..book[0,1]"}

# Title

Use `title` property to specify a title.

    :include-json: book-store.json {include: "$..book[0,1]", title: "Books"}
    
:include-json: book-store.json {include: "$..book[0,1]", title: "Books"}

# Read More

Use `readMore` to show only first lines of JSON. **Optional** `readMoreVisibleLines` can be specified to set 
the initial number of lines to display 

    :include-json: book-store.json {readMore: true, readMoreVisibleLines: 5}

:include-json: book-store.json {readMore: true, readMoreVisibleLines: 5}

# Highlights

Additionally to highlighting a specific JSON value using `paths`, you can 
(similarly to [regular code snippets](snippets/external-code-snippets#highlights)) highlight 
a line by text match or by giving a line index.

    :include-json: book-store.json {highlight: ["category", 2]}
    
:include-json: book-store.json {highlight: ["category", 2]}


# Test Results

Consider leveraging testing frameworks to extract json samples from you end points. 
Information about what assertions were made can be used to highlight points of interest.
