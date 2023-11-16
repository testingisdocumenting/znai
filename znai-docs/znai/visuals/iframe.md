# Embedding Supporting Content

Use `include-iframe` plugin to embed HTML content that supports your documentation. Some examples:
* generated React JS index.html with components that get rendered based on url anchor
* custom plot functions

```markdown {highlight: "fit: true"}
:include-iframe: iframe/custom.html {
  fit: true
}
```

:include-iframe: iframe/custom.html {
  fit: true
}

Note: Use `fit` parameter to automatically set the height of the iframe to fit its content

:include-file: iframe/custom.html {autoTitle: true}

```attention-warning
Only `iframe/custom.html` will be automatically copied to the deploy destination during documentation build step. 
If you need other supporting files you need to use [upload.txt](deployment/additional-files)
```

# Properties Override

Use `light` and `dark` to override CSS properties inside iframe content for light and dark modes respectively.
Switch mode now to see the effect.

```markdown
:include-iframe: iframe/custom.html {
  fit: true,
  light: { "--color": "#333", "--backgroundColor": "#eee" },
  dark: { "--color": "#eee", "--backgroundColor": "#333" },
}
```

:include-iframe: iframe/custom.html {
  fit: true,
  light: { "--color": "#333", "--backgroundColor": "#eee" },
  dark: { "--color": "#eee", "--backgroundColor": "#333" },
}

# Embedding Video

Use `include-iframe` to embed media from other places. By default, aspect ratio is set to `16:9`.

    :include-iframe: https://www.youtube.com/embed/tgbNymZ7vqY

:include-iframe: https://www.youtube.com/embed/tgbNymZ7vqY

# Aspect Ratio

Use `aspectRatio` to change aspect ratio of the embedded content

    :include-iframe: https://www.youtube.com/embed/tgbNymZ7vqY {aspectRatio: "4:3"}

:include-iframe: https://www.youtube.com/embed/tgbNymZ7vqY {aspectRatio: "4:3"}
