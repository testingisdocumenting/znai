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