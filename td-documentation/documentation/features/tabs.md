# Fenced Block 

To define multiple tabs we use fenced code block

    ```tabs
    JavaScript:include-file: file-name.js
    Cpp:include-cpp: simple.cpp {entry: 'main', bodyOnly: true}
    ```
    
This will generate a multi tab widget with an include plugin content per tab

```tabs
JavaScript:include-file: file-name.js
Cpp:include-cpp: simple.cpp {entry: 'main', bodyOnly: true}
```

Selecting a tab will switch all the tabs on every page.

# Markdown Per Tab

Using `include-markdown` plugin a markdown content per tab can be specified. For example installation instructions
can be specified this way.

```tabs
JavaScript:include-markdown: instructions/javascript-install.md
Cpp:include-markdown: instructions/cpp-install.md
```