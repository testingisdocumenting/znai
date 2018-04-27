# Paths

Use `include-json` plugin to bring attention to a certain place in a json file. 

    :include-json: book-store.json {paths: "root.store.book[0].category,root.store.book[2].category"}

Comma separated paths specified inside `paths` will be highlighted.

:include-json: book-store.json {paths: "root.store.book[0].category,root.store.book[2].category"}

# Include Json Path

Pass [Json Path](https://github.com/json-path/JsonPath) as `include` property 
to include only a portion of your document

    :include-json: book-store.json {include: "$..book[0,1]"}

:include-json: book-store.json {include: "$..book[0,1]"}

# Title

Use `title` property to specify a title.

    :include-json: book-store.json {include: "$..book[0,1]", title: "Books"}
    
:include-json: book-store.json {include: "$..book[0,1]", title: "Books"}


# Test Results

Consider leveraging testing frameworks to extract json samples from you end points. 
Information about what assertions were made can be used to highlight points of interest.
