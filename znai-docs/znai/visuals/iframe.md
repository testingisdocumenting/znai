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

Note: Use `fit` parameter will automatically set the height of the iframe to fit the content

:include-file: iframe/custom.html {autoTitle: true}

```attention-warning
Only `iframe/custom.html` will be automatically copied to the deploy destination during documentation build step. 
If you need other supporting files you need to use [upload.txt](deployment/additional-files)
```

# Embedding Video

Use `include-iframe` to embed content from other place. By default, aspect ratio set to `16:9`.

    :include-iframe: https://www.youtube.com/embed/tgbNymZ7vqY

:include-iframe: https://www.youtube.com/embed/tgbNymZ7vqY

# Aspect Ratio

Use `aspectRatio` to change aspect ratio of the embedded content

    :include-iframe: https://www.youtube.com/embed/tgbNymZ7vqY {aspectRatio: "4:3"}

:include-iframe: https://www.youtube.com/embed/tgbNymZ7vqY {aspectRatio: "4:3"}
