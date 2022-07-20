# Screenshots And Annotations

In [Image Annotations](visuals/image-annotations) you can specify annotations in an external file.

Using a testing framework one can generate coordinates during a UI test based on elements' placement.
Here is an example of using [WebTau](https://github.com/testingisdocumenting/webtau) testing framework to capture a screenshot and annotations

:include-file: webtauexamples/imageCapture.groovy {autoTitle: true}

```markdown {title: "znai example"}
1. Type question you want to be answered anonymously
2. Scan through results and pick the most relevant one

:include-image: doc-artifacts/duckduckgo-search.png {annotate: true}
```

1. Type question you want to be answered anonymously
2. Scan through results and pick the most relevant one

:include-image: doc-artifacts/duckduckgo-search.png {annotate: true}


:include-json: doc-artifacts/duckduckgo-search.json {autoTitle: true, paths: "root.pixelRatio"}

