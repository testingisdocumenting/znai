---
type: two-sides
---

# Setup

To enable two-sided layout as seen on this page, add the following at the beginning of a Markdown file:

```markdown 
---
type: two-sides
---
```


# Details Side

Use the right pane to display additional details. It could be a REST response or a code snippet. 

Use `include-meta` in front of some content to place it on the right side of the page. 
All the following content will go to the right side. At the start of a new section it will be reset.

```markdown 
:include-open-api: snippets/open-api/uber.json {method: "get", path: "/v1/estimates/time"}
:include-meta: {"rightSide": true}
:include-json: two-sides/price-estimate.json
```

# Open API example

:include-open-api: snippets/open-api/uber.json {method: "get", path: "/v1/estimates/time"}
:include-meta: {"rightSide": true}
:include-json: two-sides/price-estimate.json {title: "Response"}

# Aligning Data in Columns

Use an `include-empty-block` to align code snippets or other data block on both sides when there is an extra text on either side.
:include-empty-block: {rightSide: true}

:include-file: snippets/file-name.js
:include-meta: {"rightSide": true}
:include-file: snippets/file-name-with-comments.js

:include-meta: {"rightSide": false}

Two code snippets above are aligned by using an empty block on the right side. 
:include-file: two-sides-pages.md {startLine: "Aligning Data in Columns", exclude: true, endLine: ":include-meta: {\"rightSide\": false}"}
