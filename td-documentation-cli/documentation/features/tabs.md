# Fenced Block 

To define multiple tabs we use fenced code block

    ```tabs
    JavaScript: :include-file: file-name.js
    Java: :include-file: wide-code.java
    Cpp: :include-cpp: simple.cpp {entry: 'main', bodyOnly: true}
    ```
    
This will generate a multi tab widget with an include plugin content per tab

```tabs
JavaScript: :include-file: file-name.js
Java: :include-file: wide-code.java
Cpp: :include-cpp: simple.cpp {entry: 'main', bodyOnly: true}
```

Selecting a tab will switch all the tabs on every page.

# Markdown Per Tab

Any valid markdown can be used in place of a content of each tab.
Typical use case for *installation instructions*: extract differences per language or environment 

    ````tabs
    JavaScript: 
    First you need to download WebStorm and then run following using your terminal
    
    ```bash
    $ yarn install
    $ yarn start
    ```
        
    Java: 
    First you need to download Intellij IDEA and then run following using your terminal
        
    ```bash
    $ mvn install
    $ mvn exec:exec
    ```
        
    Cpp: 
    First you need to download CLion and then run following using your terminal
    
    ```bash
    $ pwd
    $ whoamis
    ```
    ````
    
````tabs
JavaScript: 
First you need to download WebStorm and then run following using your terminal

```bash
$ yarn install
$ yarn start
```
    
Java: 
First you need to download Intellij IDEA and then run following using your terminal
    
```bash
$ mvn install
$ mvn exec:exec
```
    
Cpp: 
First you need to download CLion and then run following using your terminal

```bash
$ pwd
$ whoamis
```
````

# Separate Markdown files

Consider to extract content to separate markup files if content becomes elaborate.
Use `include-markdown` plugin to include an external content.

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
Remember if you put 4+ spaces your paragraph will become a code snippet

    
    ````tabs
    JavaScript: 
    
    ```bash
    $ yarn install
    $ yarn start
    ```
    
     Avoid: committing node_modules
        
    Java: 
    First you need to download Intellij IDEA and then run following using your terminal
        
    ```bash
    $ mvn install
    $ mvn exec:exec
    ```
        
     Warning: `mvn` install for the first time may take considerable amount of time
    
    Cpp: 
    First you need to download CLion and then run following using your terminal
    
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
First you need to download Intellij IDEA and then run following using your terminal
    
```bash
$ mvn install
$ mvn exec:exec
```
    
 Warning: `mvn` install for the first time may take considerable amount of time

Cpp: 
First you need to download CLion and then run following using your terminal

```bash
$ pwd
$ whoamis
```

 Question: is C++ awesome or what?
````