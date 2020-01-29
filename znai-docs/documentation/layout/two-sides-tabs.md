---
type: two-sides
---

# Unified Tabs

In two sides layout tabs selection is moved to a single place (default is top right panel).

# Definition 

To define multiple tabs we use fenced code block

    ```tabs
    JavaScript: :include-file: snippets/file-name.js
    Java: :include-file: snippets/WideCode.java 
    Cpp: :include-cpp: snippets/simple.cpp {entry: 'main', bodyOnly: true}
    ```
    
This will generate a multi-tab widget with an include plugin content per tab.

Note: Selecting a tab will switch all the tabs on every page.

:include-meta: {rightSide: true}

```tabs
JavaScript: :include-file: snippets/file-name.js
Java: :include-file: snippets/WideCode.java 
Cpp: :include-cpp: snippets/simple.cpp {entry: 'main', bodyOnly: true}
```

# Markdown Per Tab

Any valid Markdown can be used as the content of each tab.
Typical use case for *installation instructions*: extract differences per language or environment 

    ````tabs
    JavaScript: 
    First you need to download WebStorm and then run the following using your terminal
    
    ```bash
    $ yarn install
    $ yarn start
    ```
        
    Java: 
    First you need to download Intellij IDEA and then run the following using your terminal
        
    ```bash
    $ mvn install
    $ mvn exec:exec
    ```
        
    Cpp: 
    First you need to download CLion and then run the following using your terminal
    
    ```bash
    $ pwd
    $ whoamis
    ```
    ````
    
:include-meta: {rightSide: true}
    
````tabs
JavaScript: 
First you need to download WebStorm and then run the following using your terminal

```bash
$ yarn install
$ yarn start
```
    
Java: 
First you need to download Intellij IDEA and then run the following using your terminal
    
```bash
$ mvn install
$ mvn exec:exec
```
    
Cpp: 
First you need to download CLion and then run the following using your terminal

```bash
$ pwd
$ whoamis
```
````
