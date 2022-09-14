# Beautiful and Maintainable User Guide

`Znai` combines human written text with artifacts such as
`code`, `graphs`, `REST API`, `Java Docs`, `Doxygen`, etc. 
to create up-to-date, maintainable, beautiful **User Guides** and **Tutorials**.   

:include-flow-chart: artifacts-flow.json {vertical: true, highlight: "userguide"}

:include-file: what-is-this.md {endLine: ":include-flow-chart"}

# Extensive Plugins System

`Znai` extends markdown with plugins system to supercharge visuals and maintainability.
Three categories of plugins are available for usage and creation

### Include plugins {style: "api"}

```markdown {title: "json plugin example"}
:include-json: example.json {
  title: "JSON example",
  paths: ["root.person.id"],
  collapsedPaths: ["root.details"]
}
```

:include-json: example.json {
  title: "JSON example",
  paths: ["root.person.id"],
  collapsedPaths: ["root.details"] 
}

```markdown {title: "java plugin example"}
:include-java: ../java/HelloWorld.java {
  entry: "sampleMethod", 
  bodyOnly: true,
  commentsType: "inline"
}
```

:include-java: ../java/HelloWorld.java {
  entry: "sampleMethod", 
  bodyOnly: true,
  commentsType: "inline"
}


### Inlined Code plugins {style: "api"}

```markdown {title: "plugin example"}
It is a `:icon: cloud` day, `:icon: clock` is ticking
```

It is a `:icon: cloud` day, `:icon: clock` is ticking

### Fence plugins {style: "api"}

````markdown {title: "plugin example"}
```tabs
C++: 
    content of C++ tab
Java:
    content of Java tab
Python:
    content of Python tab
```
````

```tabs
C++: 
    content of C++ tab
Java:
    content of Java tab
Python:
    content of Python tab
```

# Rich Visuals

Leverage multiple out-of-the box plugins to render charts, flow diagrams, annotated images, dynamic SVGs, etc

:include-image: znai-charts.png {pixelRatio: 2.0}

:include-image: znai-cards.png {pixelRatio: 2.0}

# Dark/Light Runtime Mode

Generate one documentation and let your users switch Dark/Light theme at runtime

:include-image: znai-flow-diagram.png {pixelRatio: 2.0}

# Two Sides Page Layout

Use [two sides](layout/two-sides-tabs) layout option to render examples and supporting information side by side
with convenient examples language switch

:include-image: znai-two-sides-tabs.png {pixelRatio: 2.0}

# Local Search

Local search with preview and instant navigation to the result 

:include-image: znai-search.png {pixelRatio: 2.0}

# Auto Presentation

With a click of a button, `Znai` turns **User Guide** content to presentation slides.
Single source of truth and minimal effort. 

Present a feature in a meeting and then share the same content as a link to the documentation.

:include-image: znai-presentation.png {pixelRatio: 2.0}

# Batteries Included

Znai comes with

* Markdown with custom extensions and dozens of plugins:
  * Content from external files with markers and filters support
  * Simplified extraction of a function body content (working with examples)
  * Embedding of JavaDoc/PyDoc documentation text, preserving styles
  * Beautiful API documentation capabilities
  * Two Sides Page Layout with convenient examples language switch
  * Rich visuals like flow diagrams and charts
  * etc
* Dev server mode with changes highlight and auto-jump to a change
* Local search (with full preview)
* Dark/light mode runtime switch
* Presentation Mode to automatically turn your documentation into slides, using the same content
