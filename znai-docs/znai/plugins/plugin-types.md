`Znai` extends markdown with plugins system to supercharge visuals and maintainability.
Three categories of plugins are available for usage and creation:
* [Include Plugins](#include-plugins)
* [Inlined Code](#inlined-code)
* [Fenced Block](#fenced-block)


# Include Plugins

To use include plugin write following on a new line
```
:include-<pluginId>: [text param] [{key1: value1, key2: value2}]
```

```markdown {title: "json plugin example"}
:include-json: example.json {
  title: "JSON example",
  highlightValue: ["root.person.id"],
  collapsedPaths: ["root.details"]
}
```

:include-json: introduction/example.json {
  title: "JSON example",
  highlightValue: ["root.person.id"],
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

# Inlined Code 

To use inlined code write plugin id inside backticks (`) surrounded with (:)
```markdown
content `:<pluginId>: [text param] [{key1: value1, key2: value2}]`
```

```markdown {title: "plugin example"}
It is a `:icon: cloud` day, `:icon: clock` is ticking
```

It is a `:icon: cloud` day, `:icon: clock` is ticking

# Fenced Block

To use fenced block plugin specify its ids after fenced block start

`````markdown
```<pluginId> [text param] [{key1: value1, key2: value2}]
content
```
`````

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

