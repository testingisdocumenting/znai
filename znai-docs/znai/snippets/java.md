# Content Extraction

In addition to [snippets manipulation](snippets/snippets-manipulation) that is applicable to any language,
Znai can extract content of methods.

    :include-java: HelloWorld.java {
      entry: "sampleMethod",
      commentsType: "inline",
      bodyOnly: true
    }

:include-java: ../java/HelloWorld.java {
  entry: "sampleMethod",
  commentsType: "inline",
  bodyOnly: true
}

Head over to [Java Content Extraction](java/content-extraction) to learn more

# Description Extraction

Znai provides plugin to extract JavaDoc content. Use it to extract high level description and merge it with the rest of the documentation.
Convert Enums and method parameters into [API Parameters](snippets/api-parameters)

    :include-java-doc-params: HelloWorld.java {
        entry: "importantAction", 
        title: "Trading Required Parameters",
        referencesPath: "references/javadoc-references-demo.csv"
    }

:include-java-doc-params: ../java/HelloWorld.java {
  entry: "importantAction",
  title: "Trading Required Parameters",
  referencesPath: "references/javadoc-references-demo.csv"
}

Head over to [Java Description Extraction](java/description-extraction) to learn more

# Auto Reference

Auto reference similar to [Python](python/content-extraction) is planned for the future releases.

Create a [GitHub Issue](https://github.com/testingisdocumenting/znai/issues) or [Discussion](https://github.com/testingisdocumenting/znai/discussions)
to help prioritize

