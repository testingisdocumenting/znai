Add `style.css` next to `toc` file to change look and feel of the resulting documentation website.

# CSS Variables

There are dozens of css variables you can target directly first, before considering more cascading options with class names.
Use your browser inspector tool to see which ones are used.

For example, to change font sizes, use:

```css {title: "style.css"}
:root {
    --znai-regular-text-size: 16px;
    --znai-smaller-text-size: 14px;
    --znai-code-text-size: 13px;
    --znai-code-font-family: Menlo, Monaco, Consolas, "Courier New", monospace;
}
```

Note: consider only targeting variables and classes that starts with `znai-` prefix. Those are considered to be public API.

# Dark Light Themes Overrides

Example above targets both light and dark theme. If you want to target dark theme specifically, use:

```css {title: "style.css"}
.theme-znai-dark {
    --znai-code-keyword-color: #5050af;
}
```