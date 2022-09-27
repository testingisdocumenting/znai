# Embedding Content

To reduce documentation maintenance burden avoid copy and paste of code snippets.
Embed content by referencing existing files using the `:include-file:` plugin instead.  

    :include-file: file-name.js
    
This `include-` syntax will appear throughout the documentation and represents a family of custom Markdown extensions. 

:include-file: file-name.js

The file will be looked up using following rules:
* directory with a markup file
* root directory of a documentation
* all lookup paths listed in a `lookup-paths` file

# Syntax highlighting

Syntax highlighting is automatically selected based file extension. 
E.g. extensions `.c`, `.h`, `.cpp`, `.hpp` are treated as C++.  

    :include-file: simple.c
    
:include-file: simple.c

    :include-file: Hello.sc
    
:include-file: Hello.sc

Use `lang` to force a different syntax highlighting

    :include-file: simple.c {lang: "java"}
    
:include-file: simple.c {lang: "java"}

Note: File extensions and `lang` are case-insensitive.

# Title

    :include-file: file-name.js {title: "ES6 class"} 

Use the `title` property to specify a title.

:include-file: file-name.js {title: "ES6 class"} 

Use the `autoTitle` property to set `title` to be the file name.

    :include-file: file-name.js {autoTitle: true} 

:include-file: file-name.js {autoTitle: true} 

# Anchor

When you specify a title, hover mouse over it to see a clickable anchor.

Use `anchorId` to override auto generated identifier. 

    :include-file: file-name.js {autoTitle: true, anchorId: "my-code-anchor"}

:include-file: file-name.js {autoTitle: true, anchorId: "my-code-anchor"}

# Wide Code

Use the `wide` option to stretch wide code to occupy as much horizontal screen real estate as possible.  

    :include-file: WideCode.java {wide: true}
    
:include-file: WideCode.java {wide: true}

Without the `wide` option code will be aligned with the rest of the text and users can use scrollbars.   

:include-file: WideCode.java

Note: Good placement of a *Wide Code* element is at the end of a page or a section to show the full version of a code sample.

# Wrap Code

Use the `wrap` option to enable long lines wrapping.  

    :include-file: WideCode.java {wrap: true}
    
:include-file: WideCode.java {wrap: true}

# Read More

If you have a file with large code snippet and you want to initially display only a small fraction use `readMore` 
option with an **optional** `readMoreVisibleLines` option to specify a number of initial lines displayed (default is 8).

    :include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3}
    
:include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3} 

# Collapse 

Use `collapsed: true|false` to make code snippet collapsible.
Note: `title` option is required  

```markdown
:include-file: file-name.js {title: "collapsible snippet", collapsed: true}
```

:include-file: file-name.js {title: "collapsible snippet", collapsed: true}

# No Gap

Use `noGap: true` to remove top/bottom margins when there are multiple snippets in a row.

```markdown
:include-file: file-name.js {title: "part one", noGap: true, collapsed: false}
:include-file: simple.c {title: "part two", noGap: true, collapsed: false}
```

:include-file: file-name.js {title: "part one", noGap: true, collapsed: false}
:include-file: simple.c {title: "part two", noGap: true, collapsed: false}

# Highlights

Use the `highlight` option to bring readers attention to the important lines.

    :include-file: file-name.js {highlight: "export"}

:include-file: file-name.js {highlight: "export"}

It is recommended to pass a substring, but you can pass a line idx (starts from 0). 
Additionally you can combine two approaches and pass a list of things to highlight. 

    :include-file: file-name.js {highlight: ["export", 1]}

:include-file: file-name.js {highlight: ["export", 1]}

Note: Order of lines to highlight is reflected during presentation mode

Use the `highlightPath` option to highlight lines specified in a separate file. 

    :include-file: file-name.js {highlightPath: "lines-to-highlight.txt"}
    
:include-file: file-name.js {highlightPath: "lines-to-highlight.txt"}

:include-file: lines-to-highlight.txt {title: "lines-to-highlight.txt"}
