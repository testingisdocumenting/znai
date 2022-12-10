---
image: {border: true}
---

# Global Defaults

Create `plugin-params.json` file to specify default parameters per plugin.

:include-file: resources/plugin-params.json {title: "plugin-params.json"}

When the parameters file is present, any time you use [API Parameters](snippets/api-parameters) plugin without `title`, an implicit title will be present

# Page Local Defaults

Use page meta block at the start of a page to set plugin default parameters specific to the page.

You may have seen meta block example to set [Page Title](flow/names):

```
---
title: Custom Name
---
```

Now we're going to use the same block to add plugin defaults local to the page.

:include-file: default-parameters.md {startLine: "---", endLine: "---"}

Once we have a block like this, every [include-image](visuals/images#extension) plugin on this page will have `border` set to true.

```markdown
:include-image: visuals/regular-image.png
```

:include-image: visuals/regular-image.png 
