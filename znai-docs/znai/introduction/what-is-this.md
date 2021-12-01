# Beautiful and Maintainable User Guide

`Znai` combines human written text with artifacts such as
`code`, `graphs`, `REST API`, `Java Docs`, `Doxygen`, etc. 
to create up-to-date, maintainable, beautiful **User Guides** and **Tutorials**.   

:include-flow-chart: artifacts-flow.json {vertical: true, highlight: "userguide"}

:include-file: what-is-this.md {endLine: ":include-flow-chart"}

# Extensive Plugins System

`Znai` extends markdown with plugins system to supercharge visuals and maintainability.
Three categories of plugins are available for usage and creation

### Inlined Code plugins {style: "api"}

```markdown {title: "plugin example"}
It is a `:icon: cloud` day, `:icon: clock` is ticking
```

It is a `:icon: cloud` day, `:icon: clock` is ticking

### Include plugins {style: "api"}

```markdown {title: "plugin example"}
:include-json: example.json {
  title: "JSON example",
  paths: ["root.person.id"],
  collapsedPaths: ["root.details"] }
```

:include-json: example.json {
  title: "JSON example",
  paths: ["root.person.id"],
  collapsedPaths: ["root.details"] }

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

# Auto Presentation

With a click of a button, `Znai` turns **User Guide** content to presentation slides.
Single source of truth and minimal effort. 

Present a feature in a meeting and then share the same content as a link to the documentation.

:include-image: slide.png {scale: 0.5, border: true}

# Batteries Included

`Znai` comes with
* dev server mode with changes highlight and auto-jump to a change
* local search (with full preview)
* dark/light mode runtime switch
* instant switching between pages

:include-image: search.png {fit: true, border: true}