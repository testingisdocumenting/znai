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
:include-open-api: snippets/open-api/uber.json {method: "get", path: "/estimates/time"}
:include-meta: {"rightSide": true}
:include-json: two-sides/price-estimate.json
```

# Open API example

:include-open-api: snippets/open-api/uber.json {method: "get", path: "/estimates/time"}
:include-meta: {"rightSide": true}
:include-json: two-sides/price-estimate.json {title: "Response"}


