Add `extensions.json` next to your `toc` file to inject custom CSS, JavaScript,
or HTML into the generated site. This is how you bring in your own stylesheets,
load scripts used by the [javascript plugin](plugins/javascript-plugin),
embed tracking snippets, or ship extra static files along with the docs.

```json {title: "extensions.json"}
{
  "cssResources": ["custom.css"],
  "jsResources": ["custom.js"],
  "htmlResources": ["custom.html"],
  "htmlHeadResources": ["tracking.html"]
}
```

All paths are resolved relative to your documentation root (the directory that
holds the `toc` file).

# cssResources

List of CSS files to load on every page.

```json
{
  "cssResources": ["custom.css", "plugins/javascript/theme-box.css"]
}
```

Note: a file named `style.css` placed next to `toc` is picked up automatically,
no need to list it here. See [Styling](configuration/styling).

# jsResources

List of JavaScript files to load on every page. Use this to register
custom functions for the [javascript plugin](plugins/javascript-plugin).

```json
{
  "jsResources": ["custom.js", "plugins/javascript/theme-box.js"]
}
```

# htmlResources

List of HTML snippets injected into the `<body>` of every page — useful for
widgets, chat bubbles, or any markup that needs to live alongside the rendered
content.

```json
{
  "htmlResources": ["custom.html"]
}
```

# htmlHeadResources

List of HTML snippets injected into the `<head>` of every page. Common use
cases are analytics tags, preconnect hints, or extra `<meta>` entries.

```json
{
  "htmlHeadResources": ["tracking.html"]
}
```

Note: a file named `tracking.html` placed next to `toc` is picked up
automatically.

# additionalFilesToDeploy

List of extra files to copy into the deploy output. Use this for assets
referenced by your custom CSS/JS that znai would not otherwise discover —
fonts, icons, supporting JSON, and so on.

```json
{
  "additionalFilesToDeploy": ["fonts/Inter.woff2", "data/config.json"]
}
```

Note: for broader file/directory copying, see
[additional files](deployment/additional-files).
