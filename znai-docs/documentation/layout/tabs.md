# Primary Use Case

Imagine your product supports multiple ways of achieving a final result: 
* REST
* Web
* CLI
* Java
* Python
* more...

You can use tabs to allow your user to choose the method to accomplish the goal covered in the documentation. 
Tab selections will persist for users and provide streamlined experience.

Don't: use tabs as navigation experience. 
If your user needs both `Java` and `Python` executed one by one you need to show steps one after the other in proper order.
 
# Definition 

To define multiple tabs we use fenced code block:

    ```tabs
    JavaScript: :include-file: snippets/file-name.js
    Java: :include-file: snippets/WideCode.java
    Cpp: :include-cpp: snippets/simple.cpp {entry: 'main', bodyOnly: true}
    ```
    
This will generate a multi-tab widget with `include-` plugin content for each tab.

```tabs
JavaScript: :include-file: snippets/file-name.js
Java: :include-file: snippets/WideCode.java {wide: true}
Cpp: :include-cpp: snippets/simple.cpp {entry: 'main', bodyOnly: true}
```

Selecting a tab will switch all the tabs on every page.

# Markdown Per Tab

Any valid Markdown can be used in place of `include-` content. 

A typical use case for *installation instructions* might be dividing content per language or environment:

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

# Separate Markdown Files

Consider extracting content to separate markup files if content becomes long.
Use the `include-markdown` plugin to include external content.

    ```tabs
    JavaScript: :include-markdown: instructions/javascript-install.md
    Java: :include-markdown: instructions/java-install.md
    Cpp: :include-markdown: instructions/cpp-install.md
    ```

Each tab will display inlined markdown from specified files.

```tabs
JavaScript: :include-markdown: instructions/javascript-install.md
Java: :include-markdown: instructions/java-install.md
Cpp: :include-markdown: instructions/cpp-install.md
```

# Attention Signs

Use extra space(s) in front of `Note:` or other attention sign keywords to put attention signs inside a tab. 
Remember if you put 4+ spaces, your paragraph will become a code snippet.

    
    ````tabs
    JavaScript: 
    
    ```bash
    $ yarn install
    $ yarn start
    ```
    
     Avoid: committing node_modules
        
    Java: 
    First you need to download Intellij IDEA and then run the following using your terminal
        
    ```bash
    $ mvn install
    $ mvn exec:exec
    ```
        
     Warning: `mvn` install for the first time may take considerable amount of time
    
    Cpp: 
    First you need to download CLion and then run the following using your terminal
    
    ```bash
    $ pwd
    $ whoamis
    ```
    
     Question: is C++ awesome or what?
    ````

````tabs
JavaScript: 

```bash
$ yarn install
$ yarn start
```

 Do not: commit node_modules to the repository
    
Java: 
First you need to download Intellij IDEA and then run the following using your terminal
    
```bash
$ mvn install
$ mvn exec:exec
```
    
 Warning: `mvn` install for the first time may take considerable amount of time

Cpp: 
First you need to download CLion and then run the following using your terminal

```bash
$ pwd
$ whoamis
```

 Question: is C++ awesome or what?
````