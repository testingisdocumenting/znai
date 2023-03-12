---
identifier: {validationPath: "org/testingisdocumenting/znai/extensions/json/JsonBasePlugin.java"}
---

# Auto Formatting

Use `json` fence block to render auto formatted json and use extra capabilities described below
  
    ```json
    [{"key1": "value1"}, {"key2": "value2"}]
    ```

```json
[{"key1": "value1"}, {"key2": "value2"}]
```

Use `json` include plugin to read json from an external file 

:include-file: simple.json {autoTitle: true}

    :include-json: simple.json

:include-json: simple.json

# Highlight Values By Path

Use the `:identifier: highlightValue` parameter to highlight an individual value:

    ```json {highlightValue: "root[1].key2"}
    [{"key1": "value1"}, {"key2": "value2"}]
    ```

```json {highlightValue: "root[1].key2"}
[{"key1": "value1"}, {"key2": "value2"}]
```

Pass multiple values to `:identifier: highlightValue` to highlight more than one leaf value 

    :include-json: book-store.json {highlightValue: ["root.store.book[0].category", "root.store.book[2].category"]}

Comma-separated paths specified inside `:identifier: highlightValue` will be highlighted.

:include-json: book-store.json {highlightValue: ["root.store.book[0].category", "root.store.book[2].category"]}

# Highlight Values By Path From File

Use `:identifier: highlightValueFile` to specify a file with paths to highlight values.

:include-json: book-store-paths.json {autoTitle: true} 

    :include-json: book-store.json {highlightValueFile: "book-store-paths.json"}

:include-json: book-store.json {highlightValueFile: "book-store-paths.json"}

# Highlight Keys By Path

Use the `:identifier: highlightKey` parameter to highlight keys:

    ```json {highlightKey: "root[1].key2"}
    [{"key1": ["value11", "value12"]}, {"key2": ["value21", "value22"]}]
    ```

```json {highlightKey: "root[1].key2"}
[{"key1": ["value11", "value12"]}, {"key2": ["value21", "value22"]}]
```

# Highlight Keys By Path From File

Use `:identifier: highlightKeyFile` to specify a file with paths to highlight keys.

:include-json: book-store-paths.json {autoTitle: true}

    :include-json: book-store.json {highlightKeyFile: "book-store-paths.json"}

:include-json: book-store.json {highlightKeyFile: "book-store-paths.json"}

# Json Subparts

To include only a portion of your document 
pass [Json Path](https://github.com/json-path/JsonPath) as `:identifier: include` property:

    :include-json: book-store.json {include: "$..book[0,1]"}

:include-json: book-store.json {include: "$..book[0,1]"}

# Enclose In Object

Use `:identifier: encloseInObject` to wrap any JSON and include result into parent object(s)

    :include-json: book-store.json {encloseInObject: "books.rare", include: "$..book[0,1]"}

:include-json: book-store.json {encloseInObject: "books.rare", include: "$..book[0,1]"}

# Title

Use `title` parameter to specify a snippet title:

    :include-json: book-store.json {include: "$..book[0,1]", title: "Books"}
    
:include-json: book-store.json {include: "$..book[0,1]", title: "Books"}

Use `autoTitle: true` to automatically set title to specified file name

    :include-json: book-store.json {include: "$..book[0,1]", autoTitle: true}

:include-json: book-store.json {include: "$..book[0,1]", autoTitle: true}

# Anchor

When you specify a title, hover mouse over it to see a clickable anchor.

Use `anchorId` to override auto generated identifier.

:include-json: book-store.json {include: "$..book[0,1]", title: "Books", anchorId: "my-books"}

    :include-json: book-store.json {include: "$..book[0,1]", title: "Books", anchorId: "my-books"}

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

    :include-json: trader.json {
      title: "trader",
      referencesPath: "references/json-references-demo.csv"
    }

:include-file: references/json-references-demo.csv {autoTitle: true}

:include-json: trader.json {
  title: "trader",
  referencesPath: "references/json-references-demo.csv"
}

# Test Results

Below is an example of using [WebTau](https://github.com/testingisdocumenting/webtau) testing framework to make 
an HTTP call, extract JSON response and information about asserted fields to highlight.

:include-groovy: org/testingisdocumenting/testing/examples/restapi/WebTauRestAPIGroovyTest.groovy {
  title: "WebTau REST API test example",
  entry: "weather",
  bodyOnly: true
}

`http.doc.capture` captures multiple test artifacts into separate files: request/response.json, url.txt, paths.json (validated paths).

```markdown {title: "include-json using test results"}
:include-json: weather-example/response.json {
  title: "weather response example",
  highlightValueFile: "weather-example/paths.json"
}
```

:include-json: weather-example/response.json {
  title: "weather response example",
  highlightValueFile: "weather-example/paths.json"
}

# Incomplete JSON

All the features above require fully formed JSON. If you need only syntax highlighting use [include-file](snippets/external-code-snippets) plugin

    :include-file: incomplete.json

:include-file: incomplete.json 