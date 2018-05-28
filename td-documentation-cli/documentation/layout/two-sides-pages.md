---
type: two-sides
---

# Setup

To enable two sides page layout like on this page add following at the beginning of a markdown file

```markdown 
---
type: two-sides
---
```


# Details Side

Use right pane to display additional details. It could be a REST request response or a code snippet. 

Use `include-meta` in front of a content to place a content to the right side. 
All the content after that will go to the right side. At the start of a new section it will be reset.

```markdown 
:include-open-api: snippets/open-api/uber.json {method: "get", path: "/estimates/time"}
:include-meta: {"rightSide": true}
:include-json: two-sides/price-estimate.json
```

# Open API example

:include-open-api: snippets/open-api/uber.json {method: "get", path: "/estimates/time"}
:include-meta: {"rightSide": true}
:include-json: two-sides/price-estimate.json {title: "Response"}


